 function listarClientes() {
    fetch('/clientes/')
        .then(r => r.json())
        .then(data => {
             console.log("Respuesta de la API:", data);
            // Verificamos que la respuesta sea un arreglo
            if (!Array.isArray(data)) {
                console.error("La respuesta no es un arreglo:", data);
                return;  // Detenemos la ejecución si no es un arreglo
            }

            // Construcción de la tabla
            let tabla = `
            <div class="table-responsive">
            <table class="table table-sm table-striped table-hover align-middle text-center">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Dirección</th>
                        <th>Teléfono</th>
                        <th>DNI</th>
                        <th>Correo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
            `;

            // Iteramos sobre cada cliente y agregamos una fila en la tabla
            data.forEach(cliente => {
                tabla += `
                    <tr>
                        <td>${cliente.idCli}</td>
                        <td>${cliente.nombreCli}</td>
                        <td>${cliente.apellidosCli}</td>
                        <td>${cliente.direccion}</td>
                        <td>${cliente.telefono}</td>
                        <td>${cliente.dni}</td>
                        <td>${cliente.correo}</td>
                        <td>
                            <button class="btn btn-warning" onclick="editarCliente(${cliente.idCli})">Editar</button>
                            <button class="btn btn-danger" onclick="borrarCliente(${cliente.idCli})">Borrar</button>
                        </td>
                    </tr>
                `;
            });

            // Cerramos el tbody y la tabla
            tabla += `
                </tbody>
            </table>
            </div>
            `;

            // Inyectamos la tabla en el DOM
            document.getElementById('tablaResultados').innerHTML = tabla;
        })
        .catch(error => {
            console.error("Error al obtener los clientes:", error);
        });
}

async function agregarCliente() {
    const modalHTML = `
    <div id="modalFormularioAgregarCliente" class="modal fade" tabindex="-1" aria-labelledby="modalFormularioLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg"> 
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalFormularioLabel">Agregar Nuevo Cliente</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <form id="formNuevoCliente" onsubmit="event.preventDefault(); guardarCliente();">
                        <div class="row">
                            <div class="col-12 col-md-6 mb-3">
                                <label for="username" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="username" required>
                            </div>
                            <div class="col-12 col-md-6 mb-3">
                                <label for="lastname" class="form-label">Apellidos</label>
                                <input type="text" class="form-control" id="lastname" required>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12 col-md-6 mb-3">
                                <label for="email" class="form-label">Correo Electrónico</label>
                                <input type="email" class="form-control" id="email" required>
                            </div>
                            <div class="col-12 col-md-6 mb-3">
                                <label for="dni" class="form-label">DNI</label>
                                <input type="text" class="form-control" id="dni" required>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12 col-md-6 mb-3">
                                <label for="direccion" class="form-label">Dirección</label>
                                <input type="text" class="form-control" id="direccion" required>
                            </div>
                            <div class="col-12 col-md-6 mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="text" class="form-control" id="telefono" required>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12 col-md-6 mb-3">
                                <label for="password" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="password" required>
                            </div>
                        </div>

                        <div class="d-flex justify-content-center gap-3">
                            <button type="submit" class="btn btn-primary">Agregar Cliente</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    `;

    // Insertar el modal dentro de la página (donde se quiera mostrar)
    const contenedor = document.getElementById('tablaResultados');
    contenedor.innerHTML = modalHTML;

    // Mostrar el modal de agregar cliente usando Bootstrap Modal
    const myModal = new bootstrap.Modal(document.getElementById('modalFormularioAgregarCliente'));
    myModal.show();
}


// Función para guardar el cliente
async function guardarCliente() {
      const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    
    const cliente = {
        nombreCli: document.getElementById('username').value.trim(),
        apellidosCli: document.getElementById('lastname').value.trim(),
        correo: document.getElementById('email').value.trim(),
        dni: document.getElementById('dni').value.trim(),
        direccion: document.getElementById('direccion').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        contraseña: document.getElementById('password').value.trim()
    };

    // Validación de campos
    if (!cliente.nombreCli || !cliente.apellidosCli || !cliente.correo || !cliente.dni || !cliente.direccion || !cliente.telefono || !cliente.contraseña) {
        alert("¡Error! Todos los campos son obligatorios.");
        return;
    }

    // Enviar el cliente al backend
    try {
        const response = await fetch('/clientes/', {
            method: 'POST',
         headers: { 
        'Content-Type': 'application/json',
        [header]: token  // Aquí envías el token CSRF
            },
            body: JSON.stringify(cliente)
        });

        if (response.ok) {
            alert("Cliente agregado con éxito");
            listarClientes(); // Actualizar la lista de clientes
        } else {
            alert("Hubo un error al agregar el cliente. Intenta de nuevo.");
        }
    } catch (error) {
        console.error('Error al agregar el cliente:', error);
        alert("Hubo un error al agregar el cliente. Intenta de nuevo.");
    }

    // Cerrar el modal después de agregar el cliente
    const modalFormulario = bootstrap.Modal.getInstance(document.getElementById('modalFormularioAgregarCliente'));
    modalFormulario.hide();
}

async function editarCliente(id) {
    // Obtener los datos del cliente a editar desde el servidor
    try {
        const response = await fetch(`/clientes/${id}`);
        if (!response.ok) throw new Error('Cliente no encontrado');
        const cliente = await response.json();

        const modalHTML = `
        <div id="modalFormularioEditarCliente" class="modal fade" tabindex="-1" aria-labelledby="modalFormularioLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg"> 
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalFormularioLabel">Editar Cliente</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <form id="formEditarCliente" onsubmit="event.preventDefault(); actualizarCliente(${id});">
                            <div class="row">
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="username" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="username" value="${cliente.nombreCli}" required>
                                </div>
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="lastname" class="form-label">Apellidos</label>
                                    <input type="text" class="form-control" id="lastname" value="${cliente.apellidosCli}" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="email" class="form-label">Correo Electrónico</label>
                                    <input type="email" class="form-control" id="email" value="${cliente.correo}" required>
                                </div>
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="dni" class="form-label">DNI</label>
                                    <input type="text" class="form-control" id="dni" value="${cliente.dni}" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="direccion" class="form-label">Dirección</label>
                                    <input type="text" class="form-control" id="direccion" value="${cliente.direccion}" required>
                                </div>
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="telefono" class="form-label">Teléfono</label>
                                    <input type="text" class="form-control" id="telefono" value="${cliente.telefono}" required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12 col-md-6 mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="password" value="${cliente.contraseña}" required>
                                </div>
                            </div>

                            <div class="d-flex justify-content-center gap-3">
                                <button type="submit" class="btn btn-primary">Actualizar Cliente</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        `;

        // Insertar el modal en el contenedor
        const contenedor = document.getElementById('tablaResultados');
        contenedor.innerHTML = modalHTML;

        // Mostrar el modal de edición con los datos del cliente
        const myModal = new bootstrap.Modal(document.getElementById('modalFormularioEditarCliente'));
        myModal.show();
    } catch (error) {
        console.error('Error al obtener los datos del cliente:', error);
        alert('Hubo un error al cargar los datos del cliente.');
    }
}

    // Función para actualizar el cliente
    async function actualizarCliente(id) {
          const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    
    const cliente = {
        nombreCli: document.getElementById('username').value.trim(),
        apellidosCli: document.getElementById('lastname').value.trim(),
        correo: document.getElementById('email').value.trim(),
        dni: document.getElementById('dni').value.trim(),
        direccion: document.getElementById('direccion').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        contraseña: document.getElementById('password').value.trim()
    };

    // Validación de campos
    if (!cliente.nombreCli || !cliente.apellidosCli || !cliente.correo || !cliente.dni || !cliente.direccion || !cliente.telefono || !cliente.contraseña) {
        alert("¡Error! Todos los campos son obligatorios.");
        return;
    }

    // Enviar los datos al backend para actualizar el cliente
    try {
        const response = await fetch(`/clientes/${id}`, {
            method: 'PUT',
            headers: {
        'Content-Type': 'application/json',
        [header]: token
         },
         body: JSON.stringify(cliente)
        });

        if (response.ok) {
            alert("Cliente actualizado con éxito");
            listarClientes(); // Volver a cargar la lista de clientes
        } else {
            alert("Hubo un error al actualizar el cliente. Intenta de nuevo.");
        }
    } catch (error) {
        console.error('Error al actualizar el cliente:', error);
        alert("Hubo un error al actualizar el cliente. Intenta de nuevo.");
    }

    // Cerrar el modal después de actualizar el cliente
    const modalFormulario = bootstrap.Modal.getInstance(document.getElementById('modalFormularioEditarCliente'));
    modalFormulario.hide();
}


function borrarCliente(id) {
      const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    
    console.log('ID Cliente a borrar:', id);  // Verifica el ID en la consola

   fetch(`/clientes/${id}`, {
    method: 'DELETE',
    headers: {
        [header]: token
    }
})
    .then(response => {
        if (response.ok) {
            alert('Cliente eliminado');
            listarClientes(); // Volver a cargar la lista de clientes
        } else {
            alert('Cliente no encontrado');
        }
    })
    .catch(error => {
        alert('Hubo un error al eliminar el cliente');
    });
}