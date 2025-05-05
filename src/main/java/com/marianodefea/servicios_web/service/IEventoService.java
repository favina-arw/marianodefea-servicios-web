package com.marianodefea.servicios_web.service;


import java.time.LocalDate;

public interface IEventoService {
    boolean esDiaLaboral(LocalDate dia);
    //boolean esTurnoLaborable(LocalDate dia);
}
