package com.example.sqlitelab2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLHelper dbHelper;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLHelper(this);
        container = findViewById(R.id.container);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(v -> showAddDialog());
        btnShow.setOnClickListener(v -> showAllAnimals());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить животное");

        // Поля ввода
        final EditText nameInput = new EditText(this); nameInput.setHint("Кличка");
        final EditText speciesInput = new EditText(this); speciesInput.setHint("Вид");
        final EditText breedInput = new EditText(this); breedInput.setHint("Порода");
        final EditText ageInput = new EditText(this); ageInput.setHint("Возраст (лет)");
        final EditText ownerInput = new EditText(this); ownerInput.setHint("Владелец");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        layout.addView(nameInput);
        layout.addView(speciesInput);
        layout.addView(breedInput);
        layout.addView(ageInput);
        layout.addView(ownerInput);
        builder.setView(layout);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String species = speciesInput.getText().toString().trim();
            String breed = breedInput.getText().toString().trim();
            int age = 0;
            try { age = Integer.parseInt(ageInput.getText().toString().trim()); } catch (Exception e) {}
            String owner = ownerInput.getText().toString().trim();

            if (name.isEmpty() || species.isEmpty() || owner.isEmpty()) {
                Toast.makeText(this, "Кличка, вид и владелец обязательны", Toast.LENGTH_SHORT).show();
                return;
            }

            Animal animal = new Animal(0, name, species, breed, age, owner);
            long id = dbHelper.addAnimal(animal);
            if (id != -1) {
                Toast.makeText(this, "Добавлено ID: " + id, Toast.LENGTH_SHORT).show();
                showAllAnimals();
            } else {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showAllAnimals() {
        ArrayList<Animal> animals = dbHelper.getAllAnimals();
        container.removeAllViews();

        if (animals.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Нет записей");
            empty.setTextSize(18);
            container.addView(empty);
            return;
        }

        for (Animal a : animals) {
            TextView tv = new TextView(this);
            tv.setText(a.getId() + ". " + a.getName() + " (" + a.getSpecies() +
                    ", " + a.getBreed() + ", возраст " + a.getAge() +
                    " лет) – Владелец: " + a.getOwner());
            tv.setTextSize(16);
            tv.setPadding(8, 8, 8, 8);
            tv.setBackgroundColor(0xEEEEEE);
            tv.setClickable(true);
            tv.setOnClickListener(v -> showEditDialog(a));           // клик – редактирование
            tv.setOnLongClickListener(v -> {                        // долгий клик – удаление
                dbHelper.deleteAnimal(a.getId());
                Toast.makeText(this, "Удалено: " + a.getName(), Toast.LENGTH_SHORT).show();
                showAllAnimals();
                return true;
            });
            container.addView(tv);
        }
    }

    private void showEditDialog(Animal animal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Редактировать");

        final EditText nameInput = new EditText(this); nameInput.setText(animal.getName());
        final EditText speciesInput = new EditText(this); speciesInput.setText(animal.getSpecies());
        final EditText breedInput = new EditText(this); breedInput.setText(animal.getBreed());
        final EditText ageInput = new EditText(this); ageInput.setText(String.valueOf(animal.getAge()));
        final EditText ownerInput = new EditText(this); ownerInput.setText(animal.getOwner());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        layout.addView(nameInput);
        layout.addView(speciesInput);
        layout.addView(breedInput);
        layout.addView(ageInput);
        layout.addView(ownerInput);
        builder.setView(layout);

        builder.setPositiveButton("Обновить", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String species = speciesInput.getText().toString().trim();
            String breed = breedInput.getText().toString().trim();
            int age = 0;
            try { age = Integer.parseInt(ageInput.getText().toString().trim()); } catch (Exception e) {}
            String owner = ownerInput.getText().toString().trim();

            if (name.isEmpty() || species.isEmpty() || owner.isEmpty()) {
                Toast.makeText(this, "Кличка, вид и владелец обязательны", Toast.LENGTH_SHORT).show();
                return;
            }

            animal.setName(name);
            animal.setSpecies(species);
            animal.setBreed(breed);
            animal.setAge(age);
            animal.setOwner(owner);
            dbHelper.updateAnimal(animal);
            Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
            showAllAnimals();
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}