# Princípios SOLID

Os princípios SOLID são um conjunto de cinco diretrizes fundamentais que visam melhorar a qualidade e a manutenção do software, promovendo um design mais claro e compreensível. A adoção desses princípios ajuda os desenvolvedores a criar sistemas que são mais fáceis de entender, alterar e expandir ao longo do tempo. Os princípios incluem:

1. **S - Princípio da Responsabilidade Única (SRP):** Uma classe deve ter apenas uma razão para mudar. Isso significa que cada classe deve ser responsável por uma única parte da funcionalidade do software. Ao seguir esse princípio, você garante que as alterações em um aspecto específico do sistema não afetem outras partes, facilitando a manutenção e a evolução do código.

2. **O - Princípio Aberto/Fechado (OCP):** Entidades de software, como classes e módulos, devem ser abertas para extensão, mas fechadas para modificação. Isso implica que você deve ser capaz de adicionar novos comportamentos ao sistema sem alterar o código existente. Isso pode ser alcançado por meio de estratégias como o uso de interfaces e herança, permitindo que novas funcionalidades sejam acrescentadas de forma segura e organizada.

3. **L - Princípio da Substituição de Liskov (LSP):** Objetos de uma classe derivada devem poder substituir objetos da classe base sem alterar o comportamento esperado do sistema. Isso significa que, ao usar uma classe derivada, o sistema deve continuar a operar corretamente como se estivesse usando a classe base. Para respeitar esse princípio, é importante projetar hierarquias de classes de maneira que as subclasses mantenham a funcionalidade e os contratos definidos pela classe base.

4. **I - Princípio da Segregação de Interface (ISP):** Uma classe não deve ser forçada a implementar interfaces que não utiliza. Isso sugere que é melhor ter várias interfaces específicas, ao invés de uma única interface abrangente. Com isso, as classes podem implementar apenas as interfaces que realmente necessitam, evitando a implementação de métodos desnecessários e promovendo um design mais coeso e menos acoplado.

5. **D - Princípio da Inversão de Dependência (DIP):** Este princípio estabelece que as classes devem depender de abstrações, e não de classes concretas. Isso promove a flexibilidade e a testabilidade do código, pois as implementações específicas podem ser alteradas ou substituídas sem afetar o restante do sistema. A injeção de dependência é uma técnica comum para facilitar a aplicação desse princípio, permitindo que componentes de software sejam desacoplados e gerenciados de maneira mais eficaz.

## Aplicação dos Princípios SOLID no Spring Boot

### 1. Single Responsibility Principle (SRP)

**Definição:** Uma classe deve ter apenas uma razão para mudar. Isso significa que uma classe deve ter uma única responsabilidade ou função.

**Exemplo:**

```java
// Classe de Serviço de Pedido - Responsável apenas por lógica de pedidos
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(Order order) {
        // lógica de negócio para realizar um pedido
        orderRepository.save(order);
    }
}

// Classe de Serviço de Notificação - Responsável apenas por lógica de notificação
@Service
public class NotificationService {
    public void sendOrderConfirmation(Order order) {
        // lógica para enviar confirmação de pedido
        System.out.println("Confirmação de pedido enviada para: " + order.getCustomer().getEmail());
    }
}
```

### 2. Open/Closed Principle (OCP)

**Definição:** Entidades de software devem ser abertas para extensão, mas fechadas para modificação. Isso pode ser alcançado por meio de interfaces e herança.

**Exemplo:**

```java
// Interface de Pagamento
public interface PaymentMethod {
    void pay(double amount);
}

// Implementação de Pagamento por Cartão
@Component
public class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando " + amount + " com cartão de crédito.");
    }
}

// Implementação de Pagamento por PayPal
@Component
public class PayPalPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando " + amount + " com PayPal.");
    }
}

// Classe de Serviço de Pagamento
@Service
public class PaymentService {
    @Autowired
    private List<PaymentMethod> paymentMethods; // Injeção de todas as implementações

    public void processPayment(PaymentMethod paymentMethod, double amount) {
        paymentMethod.pay(amount); // Chama o método apropriado
    }
}
```

### 3. Liskov Substitution Principle (LSP)

**Definição:** Objetos de uma classe derivada devem poder substituir objetos da classe base sem alterar o comportamento desejado.

**Exemplo:**

```java
// Classe Base de Notificação
public abstract class Notification {
    public abstract void notifyUser(Order order);
}

// Notificação por Email
@Component
public class EmailNotification extends Notification {
    @Override
    public void notifyUser(Order order) {
        System.out.println("Enviando email para: " + order.getCustomer().getEmail());
    }
}

// Notificação por SMS
@Component
public class SMSNotification extends Notification {
    @Override
    public void notifyUser(Order order) {
        System.out.println("Enviando SMS para: " + order.getCustomer().getPhoneNumber());
    }
}

// Serviço de Notificação
@Service
public class NotificationService {
    @Autowired
    private List<Notification> notifications;

    public void notifyAll(Order order) {
        for (Notification notification : notifications) {
            notification.notifyUser(order);
        }
    }
}
```

### 4. Interface Segregation Principle (ISP)

**Definição:** Uma classe não deve ser forçada a implementar interfaces que não usa. Isso significa que é melhor ter várias interfaces específicas em vez de uma interface única e grande.

**Exemplo:**

```java
// Interface de Notificação
public interface Notifiable {
    void notifyUser(Order order);
}

// Interface de Pagamento
public interface Payable {
    void pay(double amount);
}

// Implementação de Notificação
@Component
public class EmailNotification implements Notifiable {
    @Override
    public void notifyUser(Order order) {
        System.out.println("Enviando email para: " + order.getCustomer().getEmail());
    }
}

// Implementação de Pagamento
@Component
public class CreditCardPayment implements Payable {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando " + amount + " com cartão de crédito.");
    }
}
```

### 5. Dependency Inversion Principle (DIP)

**Definição:** Dependa de abstrações, não de classes concretas. Isso significa que as classes de alto nível não devem depender de classes de baixo nível, mas sim de abstrações.

**Exemplo:**

```java
// Interface de Repositório
public interface OrderRepository {
    void save(Order order);
}

// Implementação de Repositório em Memória
@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
        System.out.println("Pedido salvo na memória: " + order);
    }
}

// Implementação do Serviço
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    // Injeção de Dependência através do construtor
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(Order order) {
        orderRepository.save(order);
    }
}
```
