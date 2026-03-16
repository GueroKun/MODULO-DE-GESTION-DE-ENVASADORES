package com.example.AlmacenWurth.Embarque.controller;

import com.example.AlmacenWurth.Embarque.model.Embarque;
import com.example.AlmacenWurth.Embarque.model.EmbarqueDTO;
import com.example.AlmacenWurth.Embarque.model.EmbarqueRepository;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalle;
import com.example.AlmacenWurth.EmbarqueDetalle.model.EmbarqueDetalleRepository;
import com.example.AlmacenWurth.Usuario.model.Usuario;
import com.example.AlmacenWurth.Usuario.model.UsuarioRepository;
import com.example.AlmacenWurth.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmbarqueService {

    private final EmbarqueRepository embarqueRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmbarqueDetalleRepository embarqueDetalleRepository;


    @Value("${app.upload.dir:uploads/embarques}")
    private String uploadDir;

    public EmbarqueService(EmbarqueRepository embarqueRepository,
                           UsuarioRepository usuarioRepository,
                           EmbarqueDetalleRepository embarqueDetalleRepository) {
        this.embarqueRepository = embarqueRepository;
        this.usuarioRepository = usuarioRepository;
        this.embarqueDetalleRepository = embarqueDetalleRepository;
    }


    @Transactional
    public EmbarqueDTO subirArchivo(MultipartFile file, Long usuarioId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar un archivo.");
        }

        String nombreOriginal = file.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre válido.");
        }

        validarExtensionExcel(nombreOriginal);

        Usuario usuario = null;
        if (usuarioId != null) {
            usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + usuarioId));
        }

        try {
            Path carpeta = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(carpeta);

            String extension = obtenerExtension(nombreOriginal);
            String nombreGuardado = UUID.randomUUID() + extension;

            Path rutaDestino = carpeta.resolve(nombreGuardado);
            Files.copy(file.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            ExcelMetadata metadata = analizarExcel(rutaDestino, extension);

            Embarque embarque = new Embarque();
            embarque.setNombreArchivoOriginal(nombreOriginal);
            embarque.setNombreArchivoGuardado(nombreGuardado);
            embarque.setRutaArchivo(rutaDestino.toString());
            embarque.setFechaCarga(LocalDateTime.now());
            embarque.setEstatus(Embarque.Estatus.PENDIENTE_PROCESAR);
            embarque.setTamanoBytes(file.getSize());
            embarque.setTipoContenido(file.getContentType());
            embarque.setUsuario(usuario);
            embarque.setNumeroHojas(metadata.getNumeroHojas());
            embarque.setTotalFilas(metadata.getTotalFilas());
            embarque.setTotalColumnas(metadata.getTotalColumnas());
            embarque.setTotalCeldasConDatos(metadata.getTotalCeldasConDatos());

            Embarque embarqueGuardado = embarqueRepository.save(embarque);

            guardarDetallesExcel(rutaDestino, extension, embarqueGuardado);

            return toDTO(embarqueGuardado);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    private void guardarDetallesExcel(Path rutaArchivo, String extension, Embarque embarque) {
        Set<String> prefijosPermitidos = new HashSet<>();
        prefijosPermitidos.add("00040");
        prefijosPermitidos.add("00501");

        try (InputStream is = Files.newInputStream(rutaArchivo);
             Workbook workbook = extension.equalsIgnoreCase(".xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // empieza en 1 porque 0 es encabezado
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

                if (descripcion == null || descripcion.isBlank()) {
                    descripcion = "";
                }

                if (cantidad == null) {
                    cantidad = 0;
                }

                EmbarqueDetalle detalle = new EmbarqueDetalle();
                detalle.setEmbarque(embarque);
                detalle.setCodigo(codigoLimpio);
                detalle.setDescripcion(descripcion.trim());
                detalle.setCantidad(cantidad);
                detalle.setAbc(abc != null ? abc.trim() : null);

                embarqueDetalleRepository.save(detalle);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al leer y guardar detalle del Excel: " + e.getMessage());
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

        embarqueRepository.delete(embarque);
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

    //obtener datois especificos
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
}