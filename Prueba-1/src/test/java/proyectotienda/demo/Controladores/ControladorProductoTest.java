package proyectotienda.demo.Controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import proyectotienda.controladores.ControladorProducto;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControladorProducto.class)
class ControladorProductoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private proyectotienda.repositorios.RepositorioProducto repositorio;

    @MockBean
    private proyectotienda.servicios.ServicioProducto servicio;

    @Autowired
    private ObjectMapper objectMapper;

    private proyectotienda.entidades.Producto producto;

    @BeforeEach
    void setUp() {
        producto = new proyectotienda.entidades.Producto();
        producto.setId(1);
        producto.setNombre("Laptop");
        producto.setPrecio(1500.0);
        producto.setActivo(true);
    }

    @Test
    void listar_DebeRetornarPaginaDeProductos() throws Exception {
        Mockito.when(repositorio.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(producto)));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Laptop"));
    }

    @Test
    void crear_DebeRetornarError_SiNombreYaExiste() throws Exception {
        Mockito.when(repositorio.existsByNombre("Laptop")).thenReturn(true);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: El nombre del producto ya existe."));
    }

    @Test
    void crear_DebeGuardarProducto_SiEsValido() throws Exception {
        Mockito.when(repositorio.existsByNombre(anyString())).thenReturn(false);
        Mockito.when(repositorio.save(any(proyectotienda.entidades.Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    void detalle_DebeRetornar404_SiNoExiste() throws Exception {
        Mockito.when(repositorio.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cambiarEstado_DebeAlternarActivo() throws Exception {
        Mockito.when(repositorio.findById(1)).thenReturn(Optional.of(producto));
        Mockito.when(repositorio.save(any(proyectotienda.entidades.Producto.class))).thenReturn(producto);

        mockMvc.perform(patch("/api/productos/1/activar"))
                .andExpect(status().isOk());

        Mockito.verify(repositorio).save(any(proyectotienda.entidades.Producto.class));
    }

    @Test
    void ajustar_DebeRetornarBadRequest_SiFaltanCampos() throws Exception {
        Map<String, Object> incompleteBody = new HashMap<>();
        incompleteBody.put("cantidad", 10);

        mockMvc.perform(post("/api/productos/1/ajustar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Debe proporcionar 'cantidad' y 'razon'."));
    }

    @Test
    void ajustar_DebeLlamarAlServicio_SiBodyEsCorrecto() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("cantidad", 5);
        body.put("razon", "Entrada por compra");

        // FIX: Eliminamos los eq() innecesarios para valores literales
        Mockito.when(servicio.ajustarStock(1, 5, "Entrada por compra"))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos/1/ajustar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }
}