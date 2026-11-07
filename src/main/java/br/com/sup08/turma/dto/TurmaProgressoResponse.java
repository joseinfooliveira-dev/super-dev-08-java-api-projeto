package br.com.sup08.turma.dto;

public record TurmaProgressoResponse(
        Integer cargaHorariaContratada,
        Integer horasRealizadas,
        Integer horasRestantes,
        Long totalAlunosMatriculados
) {}