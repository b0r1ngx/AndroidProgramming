# Лабораторная работа №4. RecyclerView.

## Цели
* Ознакомиться с принципами работы adapter-based views.
* Получить практические навыки разработки адаптеров для view.

## Задачи
### Задача 1. Знакомство с библиотекой (при помощи написания unit test-ов)
Ознакомьтесь со #strict и #shuffle mode библиотеки, проиллюстрировав его работу unit-тестами.

В нашем распоряжении имеется [библиотека](biblib), предоставляющая программный доступ к записям в формате [bibtex](http://www.bibtex.org). 

Библиотека имеет 2 режима работы normal и strict. 
* **Normal mode** - обычный режим работы, без ограничений*
* **Strict mode** - работает искусственное ограничение: в памяти нельзя хранить более **BibConfig.java#maxValid=20** записей одновременно. При извлечении **maxValid+1**-й записи 1-я извелеченная запись становится невалидной (при доступе к полям кидаются исключения). Это ограничение позволит быстрее выявлять ошибки при работе с **RecyclerView** и **адаптерами**.

#### Указания
* Изучите демонстрационное предложенную [библиотеку](biblib), уделите особое внимание файлу [BibDatabaseTest.java](biblib/src/test/java/name/ank/lab4/BibDatabaseTest.java). В этом файле тестируется (демонстрируется) создание библиотеки (**setup()**), чтение одной записи (**getFirstEntry()**) и normal mode ( **normalModeDoesNotThrowException()**). Посмотрите на пример [исходных данных](biblib/src/test/resources/references.bib), при необходимости загляните [внутрь библиотеки](biblib/src/main/java/name/ank/lab4).

Действительно, заглянув в папку с реализацией библиотеки можно подробно понять, как она функционирует, а файл **BibDatabaseTest.java** - тестирует данную библиотеку, в некоторых ее особенностях.

* Напишите несколько тестов с помощью которых вы можете сами себя проверить, что правильно поняли, как работает флаг **BibConfig#strict** и **BibConfig#shuffle** (в **BibDatabaseTest.java** оставлены шаблоны методов).

Реализовали два теста:
1. **strictModeThrowsException()** - тестирует работу в режиме #strict (ограничение на количество действительных экземпляров данных, в данный ограничение на 20 экземпляров)
```java
  public void strictModeThrowsException() throws IOException {
    BibDatabase database = openDatabase("/mixed.bib");
    BibConfig cfg = database.getCfg();
    cfg.strict = true;
    boolean strictWorkedOff = false;

    //Here we save link to a first entry, and loop to reach 21 entry of Database
    BibEntry first = database.getEntry(0);
    for (int i = 1; i < cfg.maxValid + 1; i++) {
      BibEntry unused = database.getEntry(i);
    }
    //Here we try to knock at first entry
    try {
      first.getField(Keys.AUTHOR);
    } catch (IllegalStateException e) {
      strictWorkedOff = true;
    }
    assertTrue(strictWorkedOff);
  }
```
3. **shuffleFlagInMixedBib()** - тестирует работу с включенным режимом #shuffle (создавая БД, в переменную с перемешанными данными)
```java
  public void shuffleFlagInMixedBib() throws IOException {
    BibConfig shuffleOnCfg = new BibConfig();
    shuffleOnCfg.shuffle = true;

    BibDatabase firstShuffledDb = openDatabaseOnCustomCfg("/mixed.bib", shuffleOnCfg);
    BibDatabase secondShuffledDb = openDatabaseOnCustomCfg("/mixed.bib", shuffleOnCfg);

    boolean shuffleWorkedOff = false;

    String entryOfDb = firstShuffledDb.getEntry(3).getField(Keys.TITLE);
    String entryOfShuffledDb = secondShuffledDb.getEntry(3).getField(Keys.TITLE);

    if (!entryOfDb.equals(entryOfShuffledDb)) {
      shuffleWorkedOff = true;
    }
  assertTrue(shuffleWorkedOff);
  }
```
Так как на Java не пишу, а default поля у методов, вроде как не задашь, реализовал доп. функцию, для открытия .bib с заданной конфигурацией
```java
  private BibDatabase openDatabaseOnCustomCfg(String s, BibConfig cfg) throws IOException {
    try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(s))) {
      return new BibDatabase(reader, cfg);
    }
  }
```

* Соберите jar файл используя команду **./gradlew build**. Результаты сборки будут доступны по пути **build/libs/biblib.jar**. Обратите внимание, что сборка завершится ошибкой, если какие-либо тесты не проходят.

С помощью команды произвели сборку, которая прошла успешно, получили файл, с помощью которого можно работать с данной библиотекой в других проектах.

![Успешная сборка](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab04/images/build_success.png "Успешная сборка")

### Задача 2. Знакомство с RecyclerView.
Напишите Android приложение, которое выводит все записи из bibtex файла на экран, используя предложенную библиотеку и **RecyclerView**. На выбор предлагается решить одну из двух задач: 

#### Задача 2.1. Однородный список (задача обычной сложности).
В качестве исходных данных используйте файл [articles.bib](samples/articles.bib) . Обратите внимание, что все записи имеют одинаковый формат (@article).

#### Задача 2.2*. Неоднородный список (задача повышенной сложности).
В качестве исходных данных используйте файл [mixed.bib](samples/mixed.bib) . Обратите внимание, что записи имеют разный формат (@article, @misc, @inproceedings, etc). Используйте разное визуальное отображение для записей разного типа.

#### Пояснения
* При выводе записей не обязательно выводить все поля. Придумайте некоторый "адекватный" формат отображения данных. Выбор формата отображения поясните в отчете.
* Записи можно выводить списком (list), сеткой (grid) или любым другим способом.

### Решение задачи 2.1
После ознакомления с классом [**RecyclerView**](https://developer.android.com/guide/topics/ui/layout/recyclerview) (являющимся наследником ViewGroup), размещением **arcticles.bib** в папку raw, ресурсов проекта и [подключением нашей библиотеке как зависимость на прекомпилированный jar файл](https://developer.android.com/studio/projects/android-library#AddDependency), перейдем к реализации задачи в приложении.

![Успешный импорт в приложение](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab04/images/import_succes.png "Успешный импорт в приложение")

Для того, чтобы использовать **RecyclerView**, нам нужно подготовить и исполнить несколько компонентов:
* Сначало надо решить, как будут выглядеть наши элементы

LinearLayoutManager располагает элементы в одномерный список, я думаю это то, что нам надо.

* 
*


### Задача 3. Бесконечный список.
Сделайте список из предыдущей задачи бесконечным: после последнего элемента все записи повторяются, начиная с первой. 

#### Указания
* Модифицируйте код адаптера так, чтобы добиться желаемого поведения приложения
