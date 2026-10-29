package com.monja.reporteservice.service;

public class ReporteNoEncontradoException extends RuntimeException {
    public ReporteNoEncontradoException(Long id) { super("No existe el reporte con id " + id); }
}
