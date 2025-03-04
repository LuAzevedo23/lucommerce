package com.luazevedo.lucommerce.services;

import com.luazevedo.lucommerce.dto.ProductDTO;
import com.luazevedo.lucommerce.entities.Product;
import com.luazevedo.lucommerce.repositories.ProductRepository;
import com.luazevedo.lucommerce.services.exceptions.DatabaseException;
import com.luazevedo.lucommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true) //Anotation do Spring = pra não dar lock no Banco de Dados.
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado."));
        return new ProductDTO(product);

    }

    /*
    OUTRA FORMA RESUMIDA DE ESCREVER O MESMO CÓDIGO ACIMA:
    ===============================================
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
    Product product = repository.findById(id).get():
    return new ProductDTO(product);
    }
    ===============================================
         */

    @Transactional(readOnly = true) // Anotação somente de leitura
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));

    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado.");
        }
    }


        /*
            CÓDIGO SEM TRY CATCH
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
            */


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado.");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }


    /*
    CÓDIGO SEM TRY CATCH
    public void delete(Long id) {
        repository.deleteById(id);
     */


    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }
}


