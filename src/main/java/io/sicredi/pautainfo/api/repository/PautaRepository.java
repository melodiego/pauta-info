package io.sicredi.pautainfo.api.repository;

import io.sicredi.pautainfo.api.model.Pauta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends MongoRepository<Pauta, String> {

}