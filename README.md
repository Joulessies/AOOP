# Cofitearia Milktea Inventory and Sales Management System

A comprehensive JavaFX-based inventory and sales management system designed with accessibility features for PWD (Persons with Disabilities) users.

## Features

### Core Functionality

- **Inventory Management**: Real-time stock tracking, expiration date monitoring, and automated reordering alerts
- **Sales Processing**: Complete POS system with transaction recording and receipt generation
- **User Management**: Role-based access control with support for different user types
- **Reporting System**: Automated reports for sales, inventory, and business analytics

### Accessibility Features

- **High Contrast Mode**: Enhanced visibility for users with visual impairments
- **Large Text Mode**: Increased font sizes for better readability
- **Screen Reader Support**: Compatibility with Java Access Bridge for screen readers
- **Keyboard Navigation**: Full keyboard accessibility for all functions
- **PWD-Friendly Design**: Specialized interface for PWD employees

## Technology Stack

- **Java 17+**: Core programming language
- **JavaFX 21**: Modern UI framework with accessibility support
- **SQLite**: Embedded database for data storage
- **Maven**: Build and dependency management
- **Object-Oriented Programming**: Clean architecture with proper OOP principles

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── cofitearia/
│   │           └── milktea/
│   │               ├── Main.java                 # Application entry point
│   │               ├── controllers/              # UI controllers
│   │               │   ├── LoginController.java
│   │               │   ├── MainDashboardController.java
│   │               │   ├── InventoryController.java
│   │               │   ├── SalesController.java
│   │               │   └── ReportsController.java
│   │               ├── models/                   # Domain models
│   │               │   ├── User.java
│   │               │   ├── Product.java
│   │               │   ├── InventoryItem.java
│   │               │   ├── Sale.java
│   │               │   └── SaleItem.java
│   │               ├── services/                 # Business logic
│   │               │   ├── UserService.java
│   │               │   ├── ProductService.java
│   │               │   ├── InventoryService.java
│   │               │   └── SalesService.java
│   │               ├── database/                 # Data access layer
│   │               │   └── DatabaseManager.java
│   │               └── utils/                    # Utility classes
│   │                   └── AccessibilityManager.java
│   └── resources/
│       ├── fxml/                                 # FXML UI files
│       │   ├── login.fxml
│       │   ├── main_dashboard.fxml
│       │   ├── inventory.fxml
│       │   ├── sales.fxml
│       │   └── reports.fxml
│       └── images/                               # Application images
└── test/                                        # Unit tests
```

## Installation and Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Installation Steps

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd milktea-inventory-system
   ```

2. **Build the project**

   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Default Login Credentials

- **Username**: `admin`
- **Password**: `admin123`

## Usage Guide

### Accessibility Features

#### Keyboard Shortcuts

- `F1`: Show help dialog
- `F2`: Toggle high contrast mode
- `F3`: Toggle large text mode
- `F4`: Toggle accessibility mode
- `Ctrl+Shift+A`: Toggle accessibility mode
- `Alt+1-4`: Quick navigation between modules
- `Escape`: Close dialogs or cancel operations

#### For PWD Users

- All functions are accessible via keyboard navigation
- Screen reader compatibility through Java Access Bridge
- High contrast mode for better visibility
- Large text mode for easier reading
- Voice announcements for important actions

### User Roles

1. **Owner**: Full system access including user management and system settings
2. **Manager**: Inventory and sales management with reporting capabilities
3. **Staff**: Basic sales processing and inventory operations
4. **PWD Staff**: Full staff privileges with enhanced accessibility features

### Main Modules

#### Inventory Management

- Add, edit, and remove products
- Track stock levels in real-time
- Monitor expiration dates
- Set up automated reordering alerts
- Manage supplier information

#### Sales Processing

- Process customer orders
- Generate receipts
- Handle different payment methods
- Track sales transactions
- Manage customer information

#### Reports and Analytics

- Daily, weekly, and monthly sales reports
- Inventory status reports
- Low stock alerts
- Sales performance analytics
- Customer transaction history

## Database Schema

The system uses SQLite with the following main tables:

- **users**: User accounts with role-based access and accessibility preferences
- **products**: Product catalog with pricing and categorization
- **inventory_items**: Stock levels, expiration dates, and supplier information
- **sales**: Transaction records with payment and customer details
- **sale_items**: Individual items within each sale
- **stock_movements**: Audit trail for inventory changes

## Development

### Adding New Features

1. Create domain models in the `models` package
2. Implement business logic in the `services` package
3. Create UI controllers in the `controllers` package
4. Design FXML layouts in the `resources/fxml` package
5. Ensure accessibility features are implemented

### Testing

```bash
mvn test
```

### Building for Distribution

```bash
mvn clean package
```

## Contributing

When contributing to this project:

1. Ensure all new features include accessibility support
2. Follow Java coding conventions
3. Add appropriate unit tests
4. Update documentation as needed
5. Test with accessibility tools

## License

This project is developed for academic purposes as part of an Object-Oriented Programming course.

## Support

For technical support or accessibility assistance:

- Check the help system within the application (F1)
- Review the accessibility settings (F4)
- Contact the development team for PWD-specific support

## Future Enhancements

- Integration with barcode scanners
- Mobile application for inventory checking
- Advanced reporting with charts and graphs
- Integration with accounting systems
- Multi-language support
- Cloud synchronization capabilities

---

**Note**: This system is designed with inclusivity in mind, ensuring that all users, including those with disabilities, can effectively use the inventory and sales management features.
# AOOP
