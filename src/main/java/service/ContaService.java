package service;

import model.Conta;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ContaRepository;

@Service
public class ContaService {
    @Autowired
    private ContaRepository repository;


    public Conta criar(Conta conta){
        if (conta.getCpf() == null) {
            throw new IllegalArgumentException("CPF obrigatório para criar conta bancária!");
        }

        String cpfLimpo = conta.getCpf().replaceAll("[^0-9]","");
        conta.setCpf(cpfLimpo);

        if (repository.existsByCpf(cpfLimpo)){
            throw new IllegalArgumentException(("Erro! CPF já está em Uso!"));
        }
        conta.setSaldo(0.0);
        return repository.save(conta);
    }

    public Double consultarSaldo(Long id){

        Conta conta = repository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada!"));

        return conta.getSaldo();
    }

    public void apagar(Long id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
        else{
            throw new RuntimeException("Não é possível apagar. Conta não encontrada");
        }

    }
}
