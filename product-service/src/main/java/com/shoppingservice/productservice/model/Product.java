package com.shoppingservice.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
}
