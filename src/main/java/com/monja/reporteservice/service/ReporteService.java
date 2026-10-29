package com.monja.reporteservice.service;

import com.monja.reporteservice.entity.Reporte;
import com.monja.reporteservice.repository.ReporteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReporteService {
    private final ReporteRepository repository;

    public ReporteService(ReporteRepository repository) { this.repository = repository; }

    @Transactional
    public Reporte registrar(Reporte reporte) {
        reporte.setId(null);
        reporte.setEstado(1);
        return repository.save(reporte);
    }

    @Transactional(readOnly = true)
    public Page<Reporte> listar(int offset, int limit) {
        if (offset < 0 || limit < 1 || limit > 100)
            throw new IllegalArgumentException("offset debe ser >= 0 y limit debe estar entre 1 y 100");
        return repository.findAllByOrderByFechaDesc(PageRequest.of(offset / limit, limit));
    }

    @Transactional(readOnly = true)
    public Reporte obtener(Long id) {
        return repository.findById(id).orElseThrow(() -> new ReporteNoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public List<Reporte> porCiudadano(String dni) { return repository.findByDniOrderByFechaDesc(dni); }

    @Transactional
    public Reporte atender(Long id) {
        Reporte reporte = obtener(id);
        reporte.setEstado(0);
        return repository.save(reporte);
    }
}
