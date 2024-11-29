package com.proyecto.turisteando.services;

import org.springframework.transaction.annotation.Transactional;

public interface CrudService <RequestDto, ResponseDto, I>{

    @Transactional(readOnly = true)
    Iterable<ResponseDto> getAll();

    @Transactional(readOnly = true)
    ResponseDto read(I id);

    @Transactional
    ResponseDto create(RequestDto dto);

    @Transactional
    ResponseDto update(RequestDto dto, I id);

    @Transactional
    ResponseDto delete(I id);

    @Transactional
    ResponseDto toggleStatus(I id);

}
