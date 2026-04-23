package com.cavi.stocky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavi.stocky.model.Movimiento;
import com.cavi.stocky.repository.MovimientoRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<Movimiento> getMovimietos(){
        return movimientoRepository.findAll();
    }

    public Movimiento saveMovimiento(Movimiento mov){
        return movimientoRepository.save(mov);
    }

    public Movimiento getMovimientoId(Long id){
        return movimientoRepository.findById(id).orElse(null);
    }

    public Movimiento updateMovimiento(Movimiento mov){
        if(!movimientoRepository.existsById(mov.getId())){
            return null;
        }
        return movimientoRepository.save(mov);
    }

    public void eliminarMovimiento(Long id){
        movimientoRepository.deleteById(id);
    }
}
