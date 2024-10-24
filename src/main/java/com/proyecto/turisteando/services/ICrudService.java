package com.proyecto.turisteando.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ICrudService<D, I> {

    @Transactional(readOnly = true)
    Iterable<D> getAll();

    @Transactional(readOnly = true)
    D read(I id);

    @Transactional
    D create(D dto);

    @Transactional
    D update(D dto, I id);

    @Transactional
    D delete(I id);

    @Transactional
    D toggleStatus(I id);

}