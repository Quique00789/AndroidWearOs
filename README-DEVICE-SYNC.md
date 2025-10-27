# Sistema de Comunicación Dispositivos - Contador Sincronizado

## 📱 ↔️ ⏱️ Descripción del Proyecto

Este proyecto demuestra la comunicación bidireccional entre un dispositivo móvil Android y un smartwatch Wear OS utilizando la **Data Layer API**. El sistema permite incrementar un contador desde el teléfono móvil y ver la actualización en tiempo real en el smartwatch.

## ⚙️ Características Implementadas

### **Dispositivo Móvil (Emisor)**
- Botón para incrementar contador
- Botón para reiniciar contador
- Visualización del valor actual
- Estado de envío hacia el smartwatch
- Interfaz intuitiva y responsive

### **Wear OS (Receptor)**
- Recepción automática de datos del móvil
- Actualización en tiempo real del contador
- Indicador de último incremento recibido
- Interfaz optimizada para pantallas pequeñas

## 🔧 Requisitos Técnicos

### **Software**
- Android Studio Arctic Fox o superior
- Android SDK API 30+ (Wear OS 3.0+)
- Google Play Services
- Dispositivos con Bluetooth habilitado

### **Hardware**
- Dispositivo Android (teléfono o tablet)
- Smartwatch con Wear OS
- Ambos dispositivos vinculados vía Bluetooth

## 🚀 Configuración del Proyecto

### **Paso 1: Preparación del Entorno**

1. Clona el repositorio y cambia a la rama `device-sync-counter`:
```bash
git clone https://github.com/Quique00789/AndroidWearOs.git
cd AndroidWearOs
git checkout device-sync-counter
```

2. Abre el proyecto en Android Studio

3. Sincroniza dependencias Gradle

### **Paso 2: Configuración de Módulos**

El proyecto contiene dos módulos principales:

- **`app/`**: Módulo Wear OS (Receptor)
- **`mobile/`**: Módulo Android móvil (Emisor)

### **Paso 3: Compilación e Instalación**

1. **Instalar en Smartwatch:**
   - Conecta tu smartwatch vía USB o ADB WiFi
   - Selecciona el módulo `app` en Android Studio
   - Ejecuta la aplicación (Run 'app')

2. **Instalar en Móvil:**
   - Conecta tu dispositivo móvil
   - Selecciona el módulo `mobile`
   - Ejecuta la aplicación (Run 'mobile')

## 🔌 Arquitectura de Comunicación

### **Data Layer API**

El sistema utiliza la Data Layer API de Google para comunicación confiable entre dispositivos:

```java
// Envío de datos (Móvil)
PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/counter");
putDataMapReq.getDataMap().putInt("counter_value", currentCounter);
PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
putDataReq.setUrgent(); // Envío prioritario

Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
```

```java
// Recepción de datos (Wear OS)
@Override
public void onDataChanged(DataEventBuffer dataEvents) {
    for (DataEvent event : dataEvents) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            DataItem item = event.getDataItem();
            if ("/counter".equals(item.getUri().getPath())) {
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                updateCounter(dataMap.getInt("counter_value"));
            }
        }
    }
}
```

### **Características de Sincronización**

- **Persistencia**: Los datos se mantienen aunque la aplicación se cierre
- **Sincronización automática**: Updates inmediatos al reconectar dispositivos
- **Manejo de errores**: Feedback visual de estado de envío
- **Optimización de batería**: Uso eficiente de recursos

## 📱 Uso de la Aplicación

### **En el Dispositivo Móvil:**
1. Abre la app "Counter Controller"
2. Presiona **"▲ Incrementar"** para aumentar el contador
3. Presiona **"🔄 Reiniciar"** para volver a cero
4. Observa el estado de envío en la parte inferior

### **En el Smartwatch:**
1. Abre la app en Wear OS
2. El contador se actualiza automáticamente
3. Se muestra el timestamp de la última actualización
4. No requiere interacción manual

## 🔊 Solución de Problemas

### **Problema: Los dispositivos no se comunican**

**Solución:**
1. Verifica que ambos dispositivos estén vinculados vía Bluetooth
2. Asegúrate de que Google Play Services esté actualizado
3. Reinstala ambas aplicaciones
4. Revisa los logs en Android Studio:
   ```bash
   adb logcat | grep "WearMainActivity\|MobileMainActivity"
   ```

### **Problema: Datos no se sincronizan inmediatamente**

**Solución:**
- El envío utiliza `.setUrgent()` para prioridad alta
- En conexiones lentas puede haber delay de 1-3 segundos
- Verifica la calidad de la conexión Bluetooth

### **Problema: App se cierra inesperadamente**

**Solución:**
1. Verifica que las dependencias estén correctas en `build.gradle`
2. Asegúrate de tener API 30+ en el smartwatch
3. Revisa los permisos en AndroidManifest.xml

## 📊 Consideraciones de Rendimiento

- **Uso de Batería**: Mínimo impacto debido a Data Layer API optimizada
- **Ancho de Banda**: Solo se envían datos cuando cambian
- **Latencia**: Típicamente <500ms en condiciones normales
- **Confiabilidad**: Data Layer API garantiza entrega de datos

## 🔮 Extensiones Futuras

- **Comunicación bidireccional**: Permitir incrementar desde el smartwatch
- **Múltiples contadores**: Varios valores sincronizados
- **Historial**: Almacenar historial de incrementos
- **Notificaciones**: Alertas en cambios importantes

---

**Nota**: Este proyecto es una demostración educativa de comunicación entre dispositivos Android y Wear OS utilizando las mejores prácticas de desarrollo.