package com.api.filmes.resources;

import java.util.List;

import javax.validation.Valid;

import com.api.filmes.models.Filme;
import com.api.filmes.repository.FilmeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(value="/filmes")
public class FilmeResource {

    @Autowired
    FilmeRepository filmeRepository;

    @GetMapping
    public List<Filme> listaFilmes(){
        return filmeRepository.findAll();
    }

    @PostMapping
    public Filme salvaFilme(@RequestBody @Valid Filme filme){
        return filmeRepository.save(filme);
    }

    @PutMapping("/{id}")
    public Filme atualizaFilme(@PathVariable(value="id") long id, @RequestBody @Valid Filme filmeAtualizado){
        filmeAtualizado.setId(id);
        return filmeRepository.save(filmeAtualizado);
    }

    @GetMapping("/{id}")
    public Filme listaFilmeUnico(@PathVariable(value="id") long id){
        return filmeRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deletaFilme(@PathVariable(value="id") long id){
        filmeRepository.deleteById(id);
    }
}