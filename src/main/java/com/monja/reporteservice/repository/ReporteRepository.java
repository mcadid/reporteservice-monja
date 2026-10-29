package com.monja.reporteservice.repository;

import com.monja.reporteservice.entity.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByDniOrderByFechaDesc(String dni);
    Page<Reporte> findAllByOrderByFechaDesc(Pageable pageable);
}
