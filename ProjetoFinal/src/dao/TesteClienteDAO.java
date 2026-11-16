package dao;

import java.util.Scanner;
import model.Cliente;
import model.Endereco;

public class TesteClienteDAO {

    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);

        System.out.println("=== PROJETO PATINHAS ===");
        System.out.println("CADASTRO DE CLIENTE");

        // DADOS PESSOAIS
        System.out.print("Nome: ");
        String nome = ler.nextLine();

        System.out.print("CPF: ");
        String cpf = ler.nextLine();

        System.out.print("Telefone: ");
        String telefone = ler.nextLine();

        System.out.print("Email: ");
        String email = ler.nextLine();

        // DADOS DO CLIENTE
        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String dataNascimento = ler.nextLine();

        System.out.print("Tipo de moradia: ");
        String tipoMoradia = ler.nextLine();

        // DADOS DO ENDEREÇO
        System.out.println("\n=== ENDEREÇO ===");

        System.out.print("Rua: ");
        String rua = ler.nextLine();

        System.out.print("Número: ");
        String numero = ler.nextLine();

        System.out.print("Bairro: ");
        String bairro = ler.nextLine();

        System.out.print("Cidade: ");
        String cidade = ler.nextLine();

        System.out.print("Estado: ");
        String estado = ler.nextLine();

        System.out.print("CEP: ");
        String cep = ler.nextLine();

        // CRIAR OBJETO ENDEREÇO
        Endereco endereco = new Endereco(
                rua, numero, bairro, cidade, estado, cep
        );

        // CRIAR OBJETO CLIENTE
        Cliente cliente = new Cliente(
                dataNascimento,
                tipoMoradia,
                0,
                nome,
                cpf,
                telefone,
                email,
                endereco
        );

        // CADASTRAR NO BANCO
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.inserirCliente(cliente);

    }
}
