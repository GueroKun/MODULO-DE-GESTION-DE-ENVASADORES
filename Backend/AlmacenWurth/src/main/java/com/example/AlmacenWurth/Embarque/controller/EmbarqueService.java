package com.example.AlmacenWurth.Embarque.controller;

import com.example.AlmacenWurth.Embarque.model.Embarque;
import com.example.AlmacenWurth.Embarque.model.EmbarqueDTO;
import com.example.AlmacenWurth.Embarque.model.EmbarquePreviewDTO;
import com.example.AlmacenWurth.Embarque.model.EmbarqueDetallePreviewDTO;
import com.example.AlmacenWurth.Embarque.model.EmbarqueRepository;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalle;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalleRepository;
import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.Usuario.model.UsuarioRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmbarqueService {

    private final EmbarqueRepository embarqueRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmbarqueDetalleRepository embarqueDetalleRepository;

    @Value("${app.upload.dir:uploads/embarques}")
    private String uploadDir;

    @Value("${app.upload.temp-dir:uploads/embarques/temp}")
    private String tempUploadDir;

    private final Map<String, PreviewTemporal> previews = new ConcurrentHashMap<>();

    public EmbarqueService(EmbarqueRepository embarqueRepository,
                           UsuarioRepository usuarioRepository,
                           EmbarqueDetalleRepository embarqueDetalleRepository) {
        this.embarqueRepository = embarqueRepository;
        this.usuarioRepository = usuarioRepository;
        this.embarqueDetalleRepository = embarqueDetalleRepository;
    }

    @Transactional(readOnly = true)
    public List<EmbarqueDTO> listar() {
        return embarqueRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmbarqueDTO obtener(Long id) {
        Embarque embarque = embarqueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Embarque no encontrado: " + id));

        return toDTO(embarque);
    }

    @Transactional
    public void eliminar(Long id) {
        Embarque embarque = embarqueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Embarque no encontrado: " + id));

        try {
            Path ruta = Paths.get(embarque.getRutaArchivo());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar el archivo físico: " + e.getMessage());
        }

        embarqueDetalleRepository.deleteByEmbarqueId(id);
        embarqueRepository.delete(embarque);
    }

    public EmbarquePreviewDTO generarPreview(MultipartFile file, Long usuarioId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar un archivo.");
        }

        String nombreOriginal = file.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre válido.");
        }

        validarExtensionExcel(nombreOriginal);

        if (usuarioId != null) {
            usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + usuarioId));
        }

        try {
            Path carpetaTemp = Paths.get(tempUploadDir).toAbsolutePath().normalize();
            Files.createDirectories(carpetaTemp);

            String extension = obtenerExtension(nombreOriginal);
            String nombreGuardado = UUID.randomUUID() + extension;
            Path rutaDestino = carpetaTemp.resolve(nombreGuardado);

            Files.copy(file.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            ExcelMetadata metadata = analizarExcel(rutaDestino, extension);
            List<EmbarqueDetallePreviewDTO> registros = leerDetallesPreview(rutaDestino, extension);

            int totalValidos = registros.size();
            int totalDescartados = Math.max(0, metadata.getTotalFilas() - totalValidos);

            String previewId = UUID.randomUUID().toString();

            EmbarquePreviewDTO previewDTO = new EmbarquePreviewDTO();
            previewDTO.setPreviewId(previewId);
            previewDTO.setNombreArchivoOriginal(nombreOriginal);
            previewDTO.setNumeroHojas(metadata.getNumeroHojas());
            previewDTO.setTotalFilasArchivo(metadata.getTotalFilas());
            previewDTO.setTotalColumnasArchivo(metadata.getTotalColumnas());
            previewDTO.setTotalCeldasConDatos(metadata.getTotalCeldasConDatos());
            previewDTO.setTotalRegistrosValidos(totalValidos);
            previewDTO.setTotalRegistrosDescartados(totalDescartados);
            previewDTO.setRegistros(registros);

            PreviewTemporal previewTemporal = new PreviewTemporal();
            previewTemporal.setPreview(previewDTO);
            previewTemporal.setRutaArchivoTemporal(rutaDestino.toString());
            previewTemporal.setExtension(extension);
            previewTemporal.setUsuarioId(usuarioId);

            previews.put(previewId, previewTemporal);

            return previewDTO;

        } catch (IOException e) {
            throw new RuntimeException("Error al generar preview del archivo: " + e.getMessage());
        }
    }

    public EmbarquePreviewDTO obtenerPreview(String previewId) {
        PreviewTemporal previewTemporal = previews.get(previewId);
        if (previewTemporal == null) {
            throw new NotFoundException("Preview no encontrada: " + previewId);
        }
        return previewTemporal.getPreview();
    }

    @Transactional
    public EmbarqueDTO confirmarPreview(String previewId) {
        PreviewTemporal previewTemporal = previews.get(previewId);
        if (previewTemporal == null) {
            throw new NotFoundException("Preview no encontrada: " + previewId);
        }

        EmbarquePreviewDTO preview = previewTemporal.getPreview();

        Usuario usuario = null;
        if (previewTemporal.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(previewTemporal.getUsuarioId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + previewTemporal.getUsuarioId()));
        }

        try {
            Path carpetaFinal = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(carpetaFinal);

            String nombreArchivoOriginal = preview.getNombreArchivoOriginal();
            String extension = previewTemporal.getExtension();
            String nombreGuardadoFinal = UUID.randomUUID() + extension;

            Path rutaTemporal = Paths.get(previewTemporal.getRutaArchivoTemporal());
            Path rutaFinal = carpetaFinal.resolve(nombreGuardadoFinal);

            Files.move(rutaTemporal, rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            Embarque embarque = new Embarque();
            embarque.setNombreArchivoOriginal(nombreArchivoOriginal);
            embarque.setNombreArchivoGuardado(nombreGuardadoFinal);
            embarque.setRutaArchivo(rutaFinal.toString());
            embarque.setFechaCarga(LocalDateTime.now());
            embarque.setEstatus(Embarque.Estatus.PENDIENTE_PROCESAR);
            embarque.setTamanoBytes(Files.size(rutaFinal));
            embarque.setTipoContenido("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            embarque.setUsuario(usuario);
            embarque.setNumeroHojas(preview.getNumeroHojas());
            embarque.setTotalFilas(preview.getTotalFilasArchivo());
            embarque.setTotalColumnas(preview.getTotalColumnasArchivo());
            embarque.setTotalCeldasConDatos(preview.getTotalCeldasConDatos());

            Embarque embarqueGuardado = embarqueRepository.save(embarque);

            for (EmbarqueDetallePreviewDTO registro : preview.getRegistros()) {
                EmbarqueDetalle detalle = new EmbarqueDetalle();
                detalle.setEmbarque(embarqueGuardado);
                detalle.setCodigo(registro.getCodigo());
                detalle.setDescripcion(registro.getDescripcion());
                detalle.setCantidad(registro.getCantidad());
                detalle.setAbc(registro.getAbc());

                embarqueDetalleRepository.save(detalle);
            }

            previews.remove(previewId);

            return toDTO(embarqueGuardado);

        } catch (IOException e) {
            throw new RuntimeException("Error al confirmar preview: " + e.getMessage());
        }
    }

    public void cancelarPreview(String previewId) {
        PreviewTemporal previewTemporal = previews.get(previewId);
        if (previewTemporal == null) {
            throw new NotFoundException("Preview no encontrada: " + previewId);
        }

        try {
            Path rutaTemporal = Paths.get(previewTemporal.getRutaArchivoTemporal());
            Files.deleteIfExists(rutaTemporal);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar el archivo temporal: " + e.getMessage());
        }

        previews.remove(previewId);
    }

    private List<EmbarqueDetallePreviewDTO> leerDetallesPreview(Path rutaArchivo, String extension) {
        Set<String> prefijosPermitidos = new HashSet<>();
        prefijosPermitidos.add("00040");
        prefijosPermitidos.add("00501");

        List<EmbarqueDetallePreviewDTO> registros = new ArrayList<>();

        try (InputStream is = Files.newInputStream(rutaArchivo);
             Workbook workbook = extension.equalsIgnoreCase(".xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String codigo = obtenerTextoCelda(row.getCell(0));
                String descripcion = obtenerTextoCelda(row.getCell(1));
                String abc = obtenerTextoCelda(row.getCell(2));
                Integer cantidad = obtenerEnteroCelda(row.getCell(4));

                if (codigo == null || codigo.isBlank()) {
                    continue;
                }

                String codigoLimpio = codigo.trim();

                if (!empiezaConPrefijoPermitido(codigoLimpio, prefijosPermitidos)) {
                    continue;
                }

                if (descripcion == null) {
                    descripcion = "";
                }

                if (cantidad == null) {
                    cantidad = 0;
                }

                EmbarqueDetallePreviewDTO detalle = new EmbarqueDetallePreviewDTO();
                detalle.setCodigo(codigoLimpio);
                detalle.setDescripcion(descripcion.trim());
                detalle.setCantidad(cantidad);
                detalle.setAbc(abc != null ? abc.trim() : null);

                registros.add(detalle);
            }

            registros.sort(Comparator.comparing(this::obtenerPrimeraLetraAbcSegura));

            return registros;

        } catch (Exception e) {
            throw new RuntimeException("Error al leer preview del Excel: " + e.getMessage());
        }
    }

    private String obtenerPrimeraLetraAbcSegura(EmbarqueDetallePreviewDTO dto) {
        if (dto.getAbc() == null || dto.getAbc().isBlank()) {
            return "Z";
        }
        return dto.getAbc().substring(0, 1).toUpperCase();
    }

    private boolean empiezaConPrefijoPermitido(String codigo, Set<String> prefijosPermitidos) {
        for (String prefijo : prefijosPermitidos) {
            if (codigo.startsWith(prefijo)) {
                return true;
            }
        }
        return false;
    }

    private String obtenerTextoCelda(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double valor = cell.getNumericCellValue();
                long entero = (long) valor;
                if (valor == entero) {
                    yield String.valueOf(entero);
                }
                yield String.valueOf(valor);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }

    private Integer obtenerEnteroCelda(Cell cell) {
        if (cell == null) return null;

        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> (int) Math.round(cell.getNumericCellValue());
                case STRING -> {
                    String texto = cell.getStringCellValue();
                    if (texto == null || texto.isBlank()) yield null;
                    yield Integer.parseInt(texto.trim());
                }
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    private ExcelMetadata analizarExcel(Path rutaArchivo, String extension) {
        try (InputStream is = Files.newInputStream(rutaArchivo);
             Workbook workbook = extension.equalsIgnoreCase(".xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is)) {

            int numeroHojas = workbook.getNumberOfSheets();
            int totalFilas = 0;
            int totalColumnas = 0;
            int totalCeldasConDatos = 0;

            for (int i = 0; i < numeroHojas; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int maxColumnasHoja = 0;

                for (Row row : sheet) {
                    boolean filaTieneDatos = false;
                    int columnasEnFila = 0;

                    for (Cell cell : row) {
                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            totalCeldasConDatos++;
                            filaTieneDatos = true;
                        }
                        columnasEnFila++;
                    }

                    if (filaTieneDatos) {
                        totalFilas++;
                    }

                    if (columnasEnFila > maxColumnasHoja) {
                        maxColumnasHoja = columnasEnFila;
                    }
                }

                if (maxColumnasHoja > totalColumnas) {
                    totalColumnas = maxColumnasHoja;
                }
            }

            ExcelMetadata metadata = new ExcelMetadata();
            metadata.setNumeroHojas(numeroHojas);
            metadata.setTotalFilas(totalFilas);
            metadata.setTotalColumnas(totalColumnas);
            metadata.setTotalCeldasConDatos(totalCeldasConDatos);

            return metadata;

        } catch (Exception e) {
            throw new RuntimeException("Error al analizar el archivo Excel: " + e.getMessage());
        }
    }

    private void validarExtensionExcel(String nombreArchivo) {
        String nombre = nombreArchivo.toLowerCase();
        if (!nombre.endsWith(".xlsx") && !nombre.endsWith(".xls")) {
            throw new IllegalArgumentException("Solo se permiten archivos Excel (.xlsx, .xls)");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        int index = nombreArchivo.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return nombreArchivo.substring(index);
    }

    private EmbarqueDTO toDTO(Embarque e) {
        EmbarqueDTO dto = new EmbarqueDTO();
        dto.setId(e.getId());
        dto.setNombreArchivoOriginal(e.getNombreArchivoOriginal());
        dto.setNombreArchivoGuardado(e.getNombreArchivoGuardado());
        dto.setRutaArchivo(e.getRutaArchivo());
        dto.setFechaCarga(e.getFechaCarga());
        dto.setEstatus(e.getEstatus().name());
        dto.setTamanoBytes(e.getTamanoBytes());
        dto.setTipoContenido(e.getTipoContenido());
        dto.setNumeroHojas(e.getNumeroHojas());
        dto.setTotalFilas(e.getTotalFilas());
        dto.setTotalColumnas(e.getTotalColumnas());
        dto.setTotalCeldasConDatos(e.getTotalCeldasConDatos());

        if (e.getUsuario() != null) {
            dto.setUsuarioId(e.getUsuario().getId());
            dto.setUsuarioNombre(e.getUsuario().getNombre());
        }

        return dto;
    }

    private static class ExcelMetadata {
        private Integer numeroHojas;
        private Integer totalFilas;
        private Integer totalColumnas;
        private Integer totalCeldasConDatos;

        public Integer getNumeroHojas() {
            return numeroHojas;
        }

        public Integer getTotalFilas() {
            return totalFilas;
        }

        public Integer getTotalColumnas() {
            return totalColumnas;
        }

        public Integer getTotalCeldasConDatos() {
            return totalCeldasConDatos;
        }

        public void setNumeroHojas(Integer numeroHojas) {
            this.numeroHojas = numeroHojas;
        }

        public void setTotalFilas(Integer totalFilas) {
            this.totalFilas = totalFilas;
        }

        public void setTotalColumnas(Integer totalColumnas) {
            this.totalColumnas = totalColumnas;
        }

        public void setTotalCeldasConDatos(Integer totalCeldasConDatos) {
            this.totalCeldasConDatos = totalCeldasConDatos;
        }
    }

    private static class PreviewTemporal {
        private EmbarquePreviewDTO preview;
        private String rutaArchivoTemporal;
        private String extension;
        private Long usuarioId;

        public EmbarquePreviewDTO getPreview() {
            return preview;
        }

        public String getRutaArchivoTemporal() {
            return rutaArchivoTemporal;
        }

        public String getExtension() {
            return extension;
        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setPreview(EmbarquePreviewDTO preview) {
            this.preview = preview;
        }

        public void setRutaArchivoTemporal(String rutaArchivoTemporal) {
            this.rutaArchivoTemporal = rutaArchivoTemporal;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }
    }
}