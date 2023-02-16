package labshoppubsub.domain;

import labshoppubsub.domain.OrderPlaced;
import labshoppubsub.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Order_table")
@Data

public class Order  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String productId;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private String customerId;
    
    
    
    
    
    private Double amount;

    @PostPersist
    public void onPostPersist(){

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        labshoppubsub.external.DecreaseStockCommand decreaseStockCommand = new labshoppubsub.external.DecreaseStockCommand();
        // mappings goes here
        OrderApplication.applicationContext.getBean(labshoppubsub.external.InventoryService.class)
            .decreaseStock(/* get???(), */ decreaseStockCommand);



        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();

    }
    @PrePersist
    public void onPrePersist(){
        // Get request from Inventory
        //labshoppubsub.external.Inventory inventory =
        //    Application.applicationContext.getBean(labshoppubsub.external.InventoryService.class)
        //    .getInventory(/** mapping value needed */);

    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }






}
