import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
class Customer {
    private String customerId;
    private String name;
    private String email;
    private LocalDateTime registeredAt;
    private int age;
    private String city;

}