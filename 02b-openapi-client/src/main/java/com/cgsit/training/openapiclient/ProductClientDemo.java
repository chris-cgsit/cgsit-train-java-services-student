package com.cgsit.training.openapiclient;

import com.cgsit.training.openapiclient.api.ProductsApi;
import com.cgsit.training.openapiclient.model.Product;

/**
 * Demo: Typsicherer REST Client — generiert aus der OpenAPI Spec.
 *
 * Voraussetzung:
 *   1. mvn generate-sources   (generiert Client-Klassen aus product-api.yaml)
 *   2. 02-rest-api-demo läuft auf WildFly (http://localhost:8080/rest-api-demo)
 *
 * Ausführen in IntelliJ: Rechtsklick auf main() → Run
 *
 * Die generierten Klassen liegen in:
 *   target/generated-sources/openapi/src/main/java/.../
 *
 * Generiert:
 *   ProductsApi   — alle Endpoints als Java-Methoden
 *   Product       — Model-Klasse mit Gettern/Settern
 *   ApiClient     — HTTP-Client Konfiguration (Base URL etc.)
 */
public class ProductClientDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== OpenAPI Generated Client Demo ===");
        System.out.println();

        // 1. API-Client erzeugen (generierte Klasse!)
        var api = new ProductsApi();

        // 2. Alle Produkte abrufen — typsicher, mit Autovervollständigung
        System.out.println("--- Alle Produkte ---");
        var products = api.getAllProducts(null);
        for (Product p : products) {
            System.out.printf("  #%d  %-20s  %.2f€  [%s]%n",
                    p.getId(), p.getName(), p.getPrice(), p.getCategory());
        }

        // 3. Ein Produkt nach ID
        System.out.println();
        System.out.println("--- Produkt #1 ---");
        var product = api.getProductById(1L);
        System.out.println("  " + product.getName() + " — " + product.getPrice() + "€");

        // 4. Neues Produkt erstellen
        System.out.println();
        System.out.println("--- Neues Produkt erstellen ---");
        var newProduct = new Product();
        newProduct.setName("Headset Pro");
        newProduct.setPrice(149.99);
        newProduct.setCategory("Peripherie");
        var created = api.createProduct(newProduct);
        System.out.println("  Erstellt: #" + created.getId() + " " + created.getName());

        // 5. Produkt aktualisieren
        System.out.println();
        System.out.println("--- Produkt #1 aktualisieren ---");
        var update = new Product();
        update.setName("Laptop Ultra");
        update.setPrice(1899.99);
        update.setCategory("Electronics");
        var updated = api.updateProduct(1L, update);
        System.out.println("  Aktualisiert: " + updated.getName() + " — " + updated.getPrice() + "€");

        // 6. Produkt löschen
        System.out.println();
        System.out.println("--- Produkt #" + created.getId() + " löschen ---");
        api.deleteProduct(created.getId());
        System.out.println("  Gelöscht.");

        // 7. Alle Produkte nochmal — Änderungen sichtbar
        System.out.println();
        System.out.println("--- Alle Produkte (nach Änderungen) ---");
        for (Product p : api.getAllProducts(null)) {
            System.out.printf("  #%d  %-20s  %.2f€  [%s]%n",
                    p.getId(), p.getName(), p.getPrice(), p.getCategory());
        }

        System.out.println();
        System.out.println("=== Fertig ===");
        System.out.println();
        System.out.println("Dieser Client wurde komplett aus der OpenAPI Spec generiert.");
        System.out.println("Keine manuellen HTTP-Aufrufe — alles typsicher mit Java-Methoden.");
    }
}
