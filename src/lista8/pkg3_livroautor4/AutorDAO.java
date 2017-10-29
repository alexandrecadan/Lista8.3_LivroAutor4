package lista8.pkg3_livroautor4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    private static Connection con;
    private PreparedStatement stmtInserir;
    private PreparedStatement stmtConsultar;
    private PreparedStatement stmtListar;
    public static int idautor;

    public AutorDAO() throws Exception {
        con = ConnectionFactory.getConnection();
        stmtInserir = con.prepareStatement("INSERT INTO autor(nome) VALUES(?)");
        stmtConsultar = con.prepareStatement("SELECT * FROM autor WHERE id = ?");
        stmtListar = con.prepareStatement("SELECT * FROM autor");
    }

    public AutorDAO(Connection con) throws Exception {
        this.con = con;
        stmtInserir = con.prepareStatement("INSERT INTO autor(nome) VALUES(?)");
        stmtConsultar = con.prepareStatement("SELECT * FROM autor WHERE id = ?");
        stmtListar = con.prepareStatement("SELECT * FROM autor");
    }

    public void inserirAutor(Autor autor) throws Exception {
        stmtInserir.setString(1, autor.getNome());
        stmtInserir.executeUpdate();
        idautor = autor.getId();
    }

    public Autor consultarAutor(int id) throws Exception {
        Autor autorLido = null;
        stmtConsultar.setInt(1, id);
        ResultSet rs = stmtConsultar.executeQuery();
        rs.next();
        autorLido = new Autor(rs.getString("nome"));
        autorLido.setId(rs.getInt("id"));
        return autorLido;
    }

    public List<Autor> listarAutores() throws Exception {
        ResultSet rs = stmtListar.executeQuery();
        List<Autor> lista = new ArrayList();
        while(rs.next()){
            Autor autor = new Autor(rs.getString("nome"));
            autor.setId(rs.getInt("id"));
            lista.add(autor);
        }
        return lista;
    }

    public void finalize() throws Throwable{
        super.finalize();
        stmtInserir.close();
        stmtConsultar.close();
        stmtListar.close();
        con.close();
    }
    //CASE 6
    public static List<Livro> lerLivro(long idAutor) throws Exception {
        //Select para pegar os livros de um autor
        String sql = "SELECT livro.id,livro.titulo"
                + " FROM livro"
                + " INNER JOIN livro_autor"
                + " 	ON livro.id = livro_autor.idLivro"
                + " WHERE livro_autor.idAutor = ? ";
        PreparedStatement stmt = null;
        List<Livro> livros = null;
        stmt = con.prepareStatement(sql);
        stmt.setLong(1, idAutor);
        ResultSet resultado = stmt.executeQuery();
        livros = new ArrayList<Livro>();
        while (resultado.next()) {
            Livro livroLido = new Livro(resultado.getString("titulo"));
            livroLido.setId(resultado.getInt("id"));
            livros.add(livroLido);
        }

        return livros;
    }
    //CASE 1
    public static void vincularLivro(int idLivro, int idAutor) throws Exception{
    	String sql = "INSERT INTO livro_autor(idLivro, idAutor) VALUES (?,?)";
    	PreparedStatement stmt = null;
    	stmt = con.prepareStatement(sql);
    	stmt.setInt(1, idLivro);
    	stmt.setInt(2, idAutor);
    	stmt.execute();
    	
    }
    //CASE 1
    public static int consultarAutorId(String nome) throws SQLException{
    	String sql = "SELECT id FROM autor WHERE nome=?";
    	PreparedStatement stmt = null;
    	stmt = con.prepareStatement(sql);
    	stmt.setString(1, nome);
    	ResultSet resultado = stmt.executeQuery();
    	resultado.next();
    	return resultado.getInt("id");
    }
}
