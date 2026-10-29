package com.monja.reporteservice.controller;

import com.monja.reporteservice.entity.Reporte;
import com.monja.reporteservice.service.ReporteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@CrossOrigin
@Validated
public class ReporteController {
    private final ReporteService service;
    public ReporteController(ReporteService service) { this.service = service; }

    @PostMapping({"", "/"})
    public ResponseEntity<Reporte> registrar(@Valid @RequestBody ReporteRequest request) {
        Reporte reporte = new Reporte();
        reporte.setDni(request.dni());
        reporte.setTipo(request.tipo().trim().toUpperCase());
        reporte.setDireccion(request.direccion().trim());
        reporte.setReferencia(request.referencia());
        reporte.setFecha(request.fecha() == null ? LocalDateTime.now() : request.fecha());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(reporte));
    }

    @GetMapping({"", "/"})
    public Page<Reporte> listar(@RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "5") int limit) {
        return service.listar(offset, limit);
    }

    @GetMapping("/{id}")
    public Reporte obtener(@PathVariable Long id) { return service.obtener(id); }

    @GetMapping("/ciudadano/{dni}")
    public List<Reporte> porCiudadano(@PathVariable @Pattern(regexp = "\\d{8}") String dni) {
        return service.porCiudadano(dni);
    }

    @PutMapping("/{id}")
    public Reporte atender(@PathVariable Long id) { return service.atender(id); }

    public record ReporteRequest(
            @NotBlank @Pattern(regexp = "\\d{8}") String dni,
            LocalDateTime fecha,
            @NotBlank @Size(max = 30) String tipo,
            @NotBlank @Size(max = 200) String direccion,
            @Size(max = 255) String referencia) {}
}
