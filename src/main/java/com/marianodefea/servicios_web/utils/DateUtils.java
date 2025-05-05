package com.marianodefea.servicios_web.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public static LocalDate getPrimerDiaDelMesAnterior(){
        return LocalDate.now().minusMonths(1).withDayOfMonth(1);
    }

    public static LocalDate getUltimoDiaDelMesAnterior(){
        LocalDate primerDia = getPrimerDiaDelMesAnterior();
        return primerDia.withDayOfMonth(primerDia.lengthOfMonth());
    }

    public static List<LocalDate> getDiasLaborablesPorRango(LocalDate inicio, LocalDate fin){
        List<LocalDate> diasLaborables = new ArrayList<>();
        for (LocalDate dia = inicio; !dia.isAfter(fin); dia = dia.plusDays(1)){
            DayOfWeek diaDeLaSemana = dia.getDayOfWeek();
            if ( diaDeLaSemana != DayOfWeek.SATURDAY && diaDeLaSemana != DayOfWeek.SUNDAY){
                diasLaborables.add(dia);
            }
        }
        return diasLaborables;
    }
}
