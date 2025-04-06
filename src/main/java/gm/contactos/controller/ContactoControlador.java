package gm.contactos.controller;

import gm.contactos.model.Contacto;
import gm.contactos.repository.ContactoRepositorio;
import gm.contactos.service.ContactoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactoControlador {

    private static final Logger logger = LoggerFactory.getLogger(ContactoControlador.class);

    @Autowired
    ContactoServicio contactoServicio;

    // Getmapping -> Procesa la peticion de tipo URL donde indicamos la URL a procesar
    @GetMapping("/")
    // ModelMap model -> Es un objeto que se utiliza para pasar datos del controlador a la vista.
    public String iniciar(ModelMap model) {
        List<Contacto> contactos = contactoServicio.listarContactos();
        contactos.forEach((contacto)-> logger.info(contacto.toString()));

        // put -> agrega un par clave-valor a el objeto model, para compartir esta informacion con la vista
        model.put("contactos", contactos);

        // Redireccionamos hacia la pagina que va a mostrar esta informacion
        return "index";
    }

    // Pagina agregar
    @GetMapping("/agregar")
    public String mostrarAgregar(){
        // Retornarnos la pagina donde tendremos nuestro formulario para agregar
        return "agregar";
    }

    // Procesamiento de la informacion del formulario de agregar
    @PostMapping("/agregar")
    // @ModelAttribute -> Especifica el objeto que se recibe de la vista
    // Contacto contacto -> El objeto que vamos a utilizar para procesarlo del lado de Java
    public String agregar(@ModelAttribute("contactoFormulario") Contacto contacto){
        logger.info("Contacto a agregar: " + contacto);
        contactoServicio.guardarContacto(contacto);

        // Redirigimos hacia la pagina iniciar para ver el cambio en la tabla
        return "redirect:/";
    }

    // Procesamiento la peticion de tipo GET para editar
    @GetMapping("/editar/{id}")
    // @PathVariable -> es una anotación utilizada para acceder a valores que se encuentran en la URL de una solicitud HTTP
    // idContacto -> Objeto que vamos a utilizar para procesar la informacion de lado de Java
    public String mostrarEditar(@PathVariable(value = "id") int idContacto, ModelMap modelo){
        Contacto contacto = contactoServicio.BuscarContactoPorId(idContacto);
        logger.info("Contacto a editar (mostrar): " + contacto);

        // Compartir el objeto con la vista para que pueda acceder a esta infor macion
        modelo.put("contacto", contacto);

        // Redirigir hacia la pagina de editar
        return "editar";
    }

    // Procesamiento de la informacion del formulario de editar
    @PostMapping("/editar")
    public String editar(@ModelAttribute("contacto") Contacto contacto){
        logger.info("Contacto a guardar: " + contacto);
        contactoServicio.guardarContacto(contacto);

        // Redireccionar al inicio para refrescar los contactos
        return "redirect:/";
    }

        // Procesamiento para eliminar un contacto
        @GetMapping("/eliminar/{id}")

        //@RequestMapping(value = "eliminar/{id}", method = RequestMethod.DELETE)
        public String eliminar(@PathVariable(value = "id") int idContacto){
            Contacto contacto = new Contacto();
            contacto.setIdContacto(idContacto);
            contactoServicio.eliminarContacto(contacto);

            // Redireccionar al inicio
            return "redirect:/";
        }
}
