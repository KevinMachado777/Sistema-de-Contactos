package gm.contactos.service;

import gm.contactos.model.Contacto;

import java.util.List;

public interface IContactoServicio {
    public List<Contacto> listarContactos();
    public Contacto BuscarContactoPorId(Integer id);
    public void guardarContacto(Contacto contacto);
    void eliminarContacto(Contacto contacto);
}
