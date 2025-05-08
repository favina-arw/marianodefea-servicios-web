package com.marianodefea.servicios_web.service;

import com.marianodefea.servicios_web.model.fichada.Fichada;
import com.marianodefea.servicios_web.model.fichada.TipoFichada;
import com.marianodefea.servicios_web.repository.IFichadaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TareasAutomaticasService {

    private static final Logger log = LoggerFactory.getLogger(TareasAutomaticasService.class);

    @Autowired
    private IFichadaRepository fichadaRepository;

    @Autowired
    private TipoFichadaService tipoFichadaService;

    @Scheduled(cron = "0 0 23 * * ?") // Ejecución diaria a las 23:00
    @Transactional
    public void cerrarFichadasAbiertas() {
        LocalDate hoy = LocalDate.now();
        log.info("🔄 Iniciando cierre automático de fichadas para la fecha: {}", hoy);

        try {
            // 1. Buscar entradas pendientes
            List<Fichada> entradasPendientes = fichadaRepository.findUltimasEntradasSinSalidaHoy(hoy);
            log.debug("📊 Fichadas a cerrar: {}", entradasPendientes.size());

            if (entradasPendientes.isEmpty()) {
                log.info("✅ No hay fichadas abiertas. Proceso terminado.");
                return;
            }

            // 2. Obtener tipo 'SALIDA'
            TipoFichada tipoSalida = tipoFichadaService.findByIdentificador('S')
                    .orElseThrow(() -> {
                        log.error("❌ Tipo de fichada 'SALIDA' no encontrado en la base de datos");
                        return new RuntimeException("Tipo de fichada 'SALIDA' no existe");
                    });

            // 3. Procesar cada entrada
            entradasPendientes.forEach(entrada -> {
                try {
                    Fichada salidaAutomatica = new Fichada();
                    salidaAutomatica.setAgente(entrada.getAgente());
                    salidaAutomatica.setTipoFichada(tipoSalida);
                    salidaAutomatica.setHora(LocalDateTime.of(hoy, LocalTime.of(23, 0)));
                    salidaAutomatica.setTipoRegistro("automático");

                    fichadaRepository.save(salidaAutomatica);
                    log.info("🔒 Cerrada automáticamente la entrada de {} (Agente ID: {})",
                            entrada.getHora(), entrada.getAgente().getId());

                } catch (Exception e) {
                    log.error("⚠️ Error al cerrar fichada para Agente ID: {}. Detalle: {}",
                            entrada.getAgente().getId(), e.getMessage());
                }
            });

            log.info("🎉 Proceso completado. Fichadas cerradas: {}", entradasPendientes.size());

        } catch (Exception e) {
            log.error("🚨 ERROR CRÍTICO en el cierre automático: {}", e.getMessage(), e);
        }
    }
}
