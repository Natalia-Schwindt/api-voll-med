package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteSinOtraConsultaEnElMismoDia implements ValidadorDeConsultas {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosReservaConsulta datos) {
        // Definimos el rango del horario de atención de la clínica para ese día
        var primerHorario = datos.fecha().withHour(7).withMinute(0).withSecond(0);
        var ultimoHorario = datos.fecha().withHour(18).withMinute(59).withSecond(59);

        var pacienteTieneOtraConsultaEnElDia = repository.existsByPacienteIdAndFechaBetween(
                datos.idPaciente(),
                primerHorario,
                ultimoHorario
        );

        if (pacienteTieneOtraConsultaEnElDia) {
            throw new ValidacionException("El paciente ya tiene una consulta reservada para ese día.");
        }
    }
}