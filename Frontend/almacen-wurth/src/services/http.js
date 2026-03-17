// src/services/http.js

const DEFAULT_HEADERS = {
  "Content-Type": "application/json",
};

function getToken() {
  return localStorage.getItem("token");
}

export async function http(url, options = {}) {
  const token = getToken();

  const headers = {
    ...(options.headers || {}),
    ...(options.body ? DEFAULT_HEADERS : {}),
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const res = await fetch(url, { ...options, headers });

  if (!res.ok) {
    // intentamos leer error como texto o json
    let message = `HTTP ${res.status}`;
    try {
      const text = await res.text();
      message = text || message;
    } catch {}
    throw new Error(message);
  }

  // si es 204 no content
  if (res.status === 204) return null;

  // si no trae body (a veces pasa)
  const contentType = res.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) return null;

  return res.json();
}
