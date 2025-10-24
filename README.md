# Android Wear OS - Interface Switcher

## 📱 Descripción del Proyecto

Aplicación para **Wear OS** que implementa un sistema de cambio de interfaces dinámico mediante un botón principal. La aplicación permite alternar entre tres modos diferentes de visualización, cada uno optimizado para diferentes casos de uso.

## ⚙️ Características Técnicas

### **Requisitos del Sistema**
- **Plataforma**: Wear OS 3.0+
- **API Level**: 30 (mínimo)
- **Target SDK**: 34
- **Lenguaje**: Java
- **IDE**: Android Studio

### **Arquitectura de Interfaces**

#### 1. **Interfaz Minimalista**
- Diseño limpio y esencial
- Elementos básicos de navegación
- Optimizada para uso rápido

#### 2. **Interfaz Detallada**
- Información completa del sistema
- Botones de acción adicionales
- Configuración y estadísticas

#### 3. **Interfaz Técnica/Desarrollador**
- Información de debugging
- Controles técnicos avanzados
- Logs y diagnósticos del sistema

## 🛠️ Implementación Técnica

### **Estructura del Proyecto**
```
app/
├── src/main/
│   ├── java/com/example/botoninterfaz/
│   │   └── MainActivity.java          # Activity principal
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml         # Layout principal
│   │   ├── drawable/
│   │   │   ├── button_primary.xml        # Estilos de botones
│   │   │   ├── status_background.xml     # Fondo de estado
│   │   │   └── content_background.xml    # Fondo de contenido
│   │   ├── values/
│   │   │   ├── colors.xml               # Paleta de colores
│   │   │   └── strings.xml              # Textos de la app
│   └── AndroidManifest.xml                  # Configuración de la app
└── build.gradle.kts                         # Dependencias del proyecto
```

### **Componentes Clave**

#### **MainActivity.java**
- Manejo de estado de interfaces (`currentInterface`)
- Métodos de alternancia (`switchInterface()`)
- Configuración dinámica de vistas
- Animaciones de transición suaves

#### **Sistema de Layouts**
- **ScrollView** principal para compatibilidad con pantallas pequeñas
- **LinearLayout** con orientación vertical
- Contenedor dinámico para cambio de interfaces

#### **Diseño Visual**
- Tema oscuro optimizado para Wear OS
- Gradientes y bordes personalizados
- Tipografía adaptativa según la interfaz

## 🔧 Configuración y Compilación

### **Dependencias Principales**
```kotlin
implementation("com.google.android.gms:play-services-wearable:18.1.0")
implementation("androidx.wear:wear:1.3.0")
implementation("androidx.core:core:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime:2.7.0")
```

### **Pasos de Instalación**
1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar dependencias Gradle
4. Configurar emulador Wear OS o dispositivo físico
5. Ejecutar en modo debug

### **Emulador Recomendado**
- **Wear OS 3 - API 30**
- Tamaño de pantalla: Round 320x320
- RAM: 1GB mínimo

## 🚀 Funcionalidades Implementadas

### **Sistema de Intercambio de Interfaces**
- Cambio cíclico entre 3 modos
- Persistencia de estado durante la sesión
- Animaciones fluidas de transición
- Actualización dinámica de contenido

### **Controles de Usuario**
- Botón principal de alternancia
- Botones de acción contextuales
- Sistema de logs en tiempo real
- Reinicio de interfaz

### **Optimizaciones para Wearables**
- Diseño adaptado a pantallas circulares
- Navegación simplificada para interacción táctil
- Uso eficiente de la batería
- Soporte para modo Always-On Display

## 🔍 Problemas Corregidos

### **Dependencias**
- ✅ Versión de Android Gradle Plugin actualizada a 8.2.2
- ✅ Target SDK corregido de 36 a 34 (versión estable)
- ✅ Dependencias de Wear OS agregadas

### **Estructura del Código**
- ✅ MainActivity.java creada desde cero
- ✅ Layouts XML implementados
- ✅ AndroidManifest.xml configurado correctamente

### **Recursos**
- ✅ Esquema de colores para tema oscuro
- ✅ Drawables con selectores de estado
- ✅ Strings externalizados para localización

## 📈 Testing y Debug

### **Métodos de Prueba**
- Probar en emulador Wear OS
- Verificar transiciones entre interfaces
- Validar responsive design en diferentes tamaños
- Testing de rendimiento de animaciones

### **Logs de Desarrollo**
La interfaz técnica incluye:
- Package name dinámico
- Interface ID actual
- Timestamp de acciones
- Estado del sistema

---

**Desarrollado para Wear OS** | **Optimizado para Android Studio** | **Compatible con API 30+**