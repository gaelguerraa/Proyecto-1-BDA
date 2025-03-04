package boletos.dtos;

public class UsuarioDTO {
    private int idUsuario;
    private String email;
    private String nombre;
    private String hashContraseña;
    private double saldo;

    public UsuarioDTO(int idUsuario, String email, String nombre, String hashContraseña, double saldo) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombre = nombre;
        this.hashContraseña = hashContraseña;
        this.saldo = saldo;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getHashContraseña() { return hashContraseña; }
    public double getSaldo() { return saldo; }
}
