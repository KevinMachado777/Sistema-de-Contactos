package gm.contactos.service;

import gm.contactos.model.Contacto;
import gm.contactos.repository.ContactoRepositorio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoServicio implements IContactoServicio {

    @Autowired
    private ContactoRepositorio contactoRepositorio;


    // Inyeccion de depedencia para ejecutar consultas SQL nativas
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Contacto> listarContactos() {
        return contactoRepositorio.findAll();
    }

    @Override
    public Contacto BuscarContactoPorId(Integer id) {
        Contacto contacto = contactoRepositorio.findById(id).orElse(null);
        return contacto;
    }

    @Override
    @Transactional // Asegura que la eliminación y el reinicio de AUTO_INCREMENT estén dentro de la misma transacción
    public void guardarContacto(Contacto contacto) {
        contactoRepositorio.save(contacto);
    }

    @Override
    // con transacción nos referimos a una unidad de trabajo que engloba varias
    // acciones, y todas esas acciones deben completarse con éxito, o ninguna de ellas tendrá efecto.
    // Esto lo hacemos con el motivo de llevar a cabo la eliminacion correcta para restaurar el id autoincrementable
    @Transactional
    public void eliminarContacto(Contacto contacto) {
        contactoRepositorio.delete(contacto);

        // Llamar el metodo que retoma el valor del Id autoincrementable
        ResetearAutoIncrement();
    }

    // Mét0do para reiniciar el valor del auto-incremento
    private void ResetearAutoIncrement() {
        // entityManager -> Instancia de JPA para interactuar con la BD
        // createNativeQuery -> Permite ejecutar consultas SQL nativas, es util cuando se necesitan operaciones que no
        // se pueden hacer con JPQL
        // "ALTER TABLE contacto AUTO_INCREMENT = 1 -> Restablece el valor del contador auto_increment
        Query query = entityManager.createNativeQuery("ALTER TABLE contacto AUTO_INCREMENT = 1");
        query.executeUpdate(); // Ejecuta la consulta SQL
    }
}
