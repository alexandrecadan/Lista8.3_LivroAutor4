package lista8.pkg3_livroautor4;

import java.sql.PreparedStatement;

import lista8.pkg3_livroautor4.LivroDAO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MainLivroAutor {
    
    private AutorDAO autorDAO;
    private LivroDAO livroDAO;

    public MainLivroAutor() throws Exception{
        autorDAO = new AutorDAO();
        livroDAO = new LivroDAO();
    }

    public static void main(String args[]) throws Exception{
        MainLivroAutor main = new MainLivroAutor();
        String opcao = "";
        while (true) {
            try{
            System.out.println("Escolha uma das opcoes e tecle <ENTER>: ");
            System.out.println("  1 - Incluir Autor");
            System.out.println("  2 - Incluir Livro");
            System.out.println("  3 - Listar Autores");
            System.out.println("  4 - Listar Livro com autores");
            System.out.println("  5 - Listar Autores de um livro");
            System.out.println("  6 - Listar Livros de um autor");
            System.out.println("  7 - Sair");
            Scanner sc = new Scanner(System.in);
            opcao = sc.nextLine();
            switch(opcao){
                case "1":
                    main.incluirAutor();
               
                    break;
                case "2":
                    main.incluirLivro();
                    break;
                case "3":
                    main.listarAutores();
                    break;
                case "4":
                    main.listarLivroComAutores();
                    break;
                case "5":
                    main.lerAutores();
                    break;
                case "6":
                	main.lerLivro();
                	break;
                default:
                    System.out.println("opcao invalida.");
                    break;
            }
            //NUNCA VAI CHEGAR NESSE IF.
            if(opcao.equals("7")){
                break;
            }
            }catch(Exception ex){
                System.out.println("Falha na operacao. Mensagem="+ ex.getMessage());
                //ex.printStackTrace();
            }
        }
    }
    //CASE 6
    public void lerLivro() throws Exception{
    	System.out.println("Digite o id do autor");
    	Scanner sc = new Scanner(System.in);
    	int id = sc.nextInt();
    	for(Livro lista : AutorDAO.lerLivro(id))
    		System.out.println(lista.getTitulo());
    }
    //CASE 5
    public void lerAutores()throws Exception{
    	System.out.println("Digite o id do livro");
    	Scanner sc = new Scanner(System.in);
    	int id = sc.nextInt();
    	for(Autor lista : LivroDAO.lerAutores(id))
    	System.out.println(lista.getNome());
    }
    //CASE 1
    public void incluirAutor() throws Exception{
        System.out.print("Digite o nome do autor:");
        Scanner sc = new Scanner(System.in);
        String nome = sc.nextLine();
        Autor autor = new Autor(nome);
        autorDAO.inserirAutor(autor);
        System.out.println("Deseja vincular um livro a este autor?\n1-SIM\n2-NAO");
        int op = sc.nextInt();
        if(op == 1){
        	System.out.println("Digite o id do livro");
        	int idLivro = sc.nextInt();
        	int idAutor = AutorDAO.consultarAutorId(nome);   
        	AutorDAO.vincularLivro(idLivro, idAutor);
        }
        else{
        	System.exit(1);
        }
    }

    public void incluirLivro() {
        System.out.print("Digite o titulo do livro:");
        Scanner sc = new Scanner(System.in);
        String titulo = sc.nextLine();
        int numAutores=1;
        List<Autor> listaAutores = new ArrayList();
        int idAutor=0;
        do{
            try{
                Scanner sc2 = new Scanner(System.in);
                System.out.print("ID Autor "+numAutores+":");
                idAutor = sc2.nextInt();
                if(idAutor==-1)
                    break;
                Autor autor = autorDAO.consultarAutor(idAutor);
                if(autor != null){
                    listaAutores.add(autor);
                    numAutores++;
                }else{
                    System.out.println("Autor nao existe!");
                }
            }
            catch(Exception ex){
                System.out.println("ID autor nao e inteiro ou invalido!");
            }
        }while(true);


        Livro livro = new Livro(titulo,listaAutores);
        livroDAO.inserirLivro(livro);
    }

    public void listarAutores() throws Exception{
        List<Autor> listaAutores = autorDAO.listarAutores();
        Collections.sort(listaAutores, new Comparator<Autor>() {
           public int compare(Autor arg0, Autor arg1) {
                return arg0.getNome().compareToIgnoreCase(arg1.getNome());
             }
        });
        System.out.println("ID\tNOME");
        for(Autor autor:listaAutores){
            System.out.println(autor.getId()+" \t"+autor.getNome());
        }
    }

    public void listarLivroComAutores() throws Exception{
        List<Livro> listaLivros = livroDAO.listarLivroComAutores();
        Collections.sort(listaLivros, new Comparator<Livro>() {
           public int compare(Livro arg0, Livro arg1) {
               String titulo = arg0.getTitulo();
               int i = titulo.compareToIgnoreCase(arg1.getTitulo());
               return i;
             }
        });
        System.out.println("ID\tTitulo do Livro\tAutores");
        for(Livro livro:listaLivros){
            System.out.print(livro.getId()+"\t"+livro.getTitulo()+"\t");
            for(Autor autor:livro.getAutores()){
                System.out.print(autor.getNome()+";");
            }
            System.out.print("\n");
        }

    }

}
