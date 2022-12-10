package com.ecommerce.ajsanta.controller;

import com.ecommerce.ajsanta.model.DetalleOrden;
import com.ecommerce.ajsanta.model.Orden;
import com.ecommerce.ajsanta.model.Producto;
import com.ecommerce.ajsanta.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    // Para almaenar los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList<>();

    // Datos de la orden
    Orden orden = new Orden();


    @GetMapping("")
    public String home(Model model) {

        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("/productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {

        LOGGER.info("Id producto enviado como parametro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto  = productoOptional.get();

        model.addAttribute("producto", producto);
        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        LOGGER.info("Producto añadido: {}", optionalProducto.get());
        LOGGER.info("Cantidad: {}", cantidad);

        return "usuario/carrito";
    }


}
