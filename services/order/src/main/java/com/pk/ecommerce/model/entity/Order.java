package com.pk.ecommerce.model.entity;

import com.pk.ecommerce.model.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;
    private String reference;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Enumerated
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    private String customerId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
}
