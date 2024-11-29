from sqlalchemy import Column, ForeignKey, Integer, String, Date, Enum
from sqlalchemy.orm import declarative_base
from datetime import date
from enum import Enum as PyEnum

from sqlalchemy import Column, Date, Integer, String

Base = declarative_base()

class Status(PyEnum):
    Active = "active"
    Banned = "banned"
    Inactive = "inactive"

class User(Base):
    __tablename__ = 'users'
    
    id: int = Column(Integer, primary_key=True, index=True)
    name: str = Column(String(50), nullable=False)
    surname: str = Column(String(50), nullable=False)
    email: str = Column(String(100), unique=True, nullable=False)
    telephone: str = Column(String(15))
    birthday: date = Column(Date)
    age: int = Column(Integer)
    registration_date: date = Column(Date)
    username: str = Column(String(50), unique=True, nullable=False)
    password: str = Column(String(100), nullable=False)
    role: str = Column(String(50), default="user")
    status: Status = Column(Enum(Status), default=Status.Active)

     # Classe discriminante per l'ereditarietà
    type = Column(String, nullable=False)

    # Polimorfismo
    __mapper_args__ = {
        'polymorphic_identity': 'user',
        'polymorphic_on': type
    }

    def __repr__(self):
        return (
            f"User(id={self.id}, name='{self.name}', surname='{self.surname}', "
            f"email='{self.email}', status={self.status})"
        )



   
  
class Admin(User):
    __tablename__ = 'admins'

    qualification: str = Column(String, nullable=False)
    id: int = Column(Integer, ForeignKey('users.id'), primary_key=True)

    __mapper_args__ = {
        'polymorphic_identity': 'admin',
    }

    def __repr__(self):
        return (
            f"Admin(id={self.id}, name='{self.name}', surname='{self.surname}', "
            f"email='{self.email}', qualification='{self.qualification}')"
        )

    







# Creazione di un oggetto User
user = User(
    id=1,
    name="John",
    surname="Doe",
    email="john.doe@example.com",
    telephone="123456789",
    birthday=date(1990, 1, 1),
    age=33,
    registration_date=date.today(),
    username="johndoe",
    password="password",
    role="user",
    status=Status.Active
)

# Creazione di un oggetto Admin
admin = Admin(
    id=2,
    name="Alice",
    surname="Smith",
    email="alice@example.com",
    telephone="987654321",
    birthday=date(1985, 5, 15),
    age=38,
    registration_date=date.today(),
    username="adminalice",
    password="securepassword",
    role="admin",
    status=Status.Active,
    qualification="MBA"
)

# Stampa degli oggetti
print(user)
print(admin)

print(user.type)
print(admin.type)







