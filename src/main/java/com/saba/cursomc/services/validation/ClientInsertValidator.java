package com.saba.cursomc.services.validation;

import com.saba.cursomc.domain.enums.TipoCliente;
import com.saba.cursomc.dto.ClienteNewDTO;
import com.saba.cursomc.resources.exception.FieldMessage;
import com.saba.cursomc.services.validation.utils.BR;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class ClientInsertValidator implements ConstraintValidator<ClientInsert, ClienteNewDTO> {

    @Override
    public void initialize(ClientInsert ann) {
    }

    @Override
    public boolean isValid(ClienteNewDTO clienteNewDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(clienteNewDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCpf(clienteNewDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if(clienteNewDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCpf(clienteNewDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        if(clienteNewDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA)){}

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldMessage()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
