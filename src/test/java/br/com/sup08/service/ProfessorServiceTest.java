package br.com.sup08.professor.service;

import br.com.sup08.common.exception.RegraNegocioException;
import br.com.sup08.professor.domain.Professor;
import br.com.sup08.professor.domain.StatusProfessor;
import br.com.sup08.professor.repository.ProfessorRepository;
import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.repository.TurmaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    public void deveLancarErroAoInativarProfessorComTurmaAtiva() {
        // 1. Cenário (Arrange)
        Professor professor = new Professor("Professor Teste");
        professor.setId(1L);
        professor.setStatus(StatusProfessor.ATIVO);

        // Fingimos que o banco achou o professor
        Mockito.when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        // Fingimos que o banco disse que SIM, tem turma ativa
        Mockito.when(turmaRepository.existsByProfessorIdAndStatus(1L, StatusTurma.ATIVA)).thenReturn(true);

        // 2. Ação e Verificação (Act & Assert)
        RegraNegocioException erro = Assertions.assertThrows(RegraNegocioException.class, () -> {
            professorService.inativar(1L);
        });

        // Verifica se a mensagem do erro é a correta (verifica o texto)
        Assertions.assertEquals("Professor não pode ser inativado pois possui turma ativa vinculada", erro.getMessage());
    }
}