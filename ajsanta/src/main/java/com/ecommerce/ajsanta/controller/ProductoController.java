package com.ecommerce.ajsanta.controller;

import com.ecommerce.ajsanta.model.Producto;
import com.ecommerce.ajsanta.model.Usuario;
import com.ecommerce.ajsanta.service.ProductoService;
import com.ecommerce.ajsanta.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);


    @Autowired
    private ProductoService productoService;


    @Autowired
    private UploadFileService upload;


    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());

        return "productos/show";
    }


    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Este es el objeto producto {} ", producto);
        Usuario u = new Usuario(1, "", "", "", "", "", "", "");
        producto.setUsuario(u);

        // imagen
        if(producto.getId() == null) {  // Cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {

        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);

        producto = optionalProducto.get();
        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }


    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

        if(file.isEmpty()) { // Editamos el producto pero no cambiamos la imagen
            Producto p = new Producto();
            p = productoService.get(producto.getId()).get();
            producto.setImagen(p.getImagen());
        } else { // Cuando se edita también la imagen
            Producto p = new Producto();
            p = productoService.get(producto.getId()).get();

            // Eliminar cuando no sea la imagen por defecto
            if(p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getNombre());
            }

            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.update(producto);
        return "redirect:/productos";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = new Producto();
        p = productoService.get(id).get();

        // Eliminar cuando no sea la imagen por defecto
        if(p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getNombre());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }




}
