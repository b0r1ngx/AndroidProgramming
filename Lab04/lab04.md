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
* **Strict mode** - работает искусственное ограничение: в памяти нельзя хранить более **BibConfig.java#maxValid=20** записей одновременно. При извлечении **maxValid+1**-й записи 1-я извлеченная запись становится не валидной (при доступе к полям кидаются исключения). Это ограничение позволит быстрее выявлять ошибки при работе с **RecyclerView** и **адаптерами**.

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

По ходу прочтения документации к **RecyclerView**, говорится о том, что нам нужно подготовить и исполнить несколько компонентов, последовательно подготовим файлы:

Для начала добавим в **activity_main.xml** элемент ViewGroup **RecyclerView**, также как до этого добавляли другие элементы
```xml
  <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        ... />
```

* **Реализация LayoutManager**, надо решить, как будут выглядеть наши элементы

**LinearLayoutManager** располагает элементы в одномерный список, я думаю это то, что нам надо, определим соответствующую переменную в **MainActivity#onCreate()**.
```kotlin
  val viewManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
```
Так же, на этом шаге надо спроектировать макет индивидуального элемента, который будет входит в **RecyclerView**, сохраним его в папке res/layout

![Дизайн индивидуального элемента](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab04/images/bibtex_item.xml.png "Дизайн индивидуального элемента")

* **Реализация ViewHolder**, содержащий макет в виде кода для индивидуального элемента в будущем списке **RecyclerView**
```kotlin
  class BibTexViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView = itemView.findViewById<TextView>(R.id.title)
        private val authorTextView = itemView.findViewById<TextView>(R.id.author)
        private val journalYearTextView = itemView.findViewById<TextView>(R.id.journal_n_year)

        fun bind(entry: BibEntry) {
            val title = parse(entry.getField(Keys.TITLE), 48)
            val author = parse(entry.getField(Keys.AUTHOR), 47)
            val journal = parse(entry.getField(Keys.JOURNAL), 41)
            
            titleTextView.text = "Title: $title"
            authorTextView.text = "Author: $author"
            journalYearTextView.text = "Journal: $journal, ${entry.getField(Keys.YEAR)}"
        }

        private fun parse(s: String, symbolsLimit: Int) =
            if (s.length > symbolsLimit) "${s.substring(0, symbolsLimit)}..." else s
    }
```
* **Реализация Adapter**, создает объекты **ViewHolder** по мере необходимости и устанавливает данные в каждый элемент **ViewHolder**
```kotlin
class ArticleAdapter(stream: InputStream):
    RecyclerView.Adapter<ArticleAdapter.BibTexViewHolder>() {
    private var database: BibDatabase

    init {
        InputStreamReader(stream).use { reader ->
            database = BibDatabase(reader)
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibTexViewHolder =
        BibTexViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bibtex_item, parent, false)
        )

    override fun onBindViewHolder(holder: BibTexViewHolder, position: Int) =
        holder.bind(database.getEntry(position))

    override fun getItemCount(): Int =
        database.size()
}
```

[Процесс связывания представлений(Views) с их данными(data) называется привязкой(binding)]

### Задача 3. Бесконечный список.
Сделайте список из предыдущей задачи бесконечным: после последнего элемента все записи повторяются, начиная с первой. 

#### Указания
* Модифицируйте код адаптера так, чтобы добиться желаемого поведения приложения

При внимательном изучении документации к **RecyclerView** , можно заметить, что у **Adapter** есть метод, **getItemCount()**, который вызывается для того, чтобы получить размер набора данных, в связи с этим, установим достаточно большое число в возвращаемое число этой функции, а обращение к конкретному элементу подправим через операцию mod, таким образом, когда **RecyclerView** понадобиться отобразить элементы превышающее количество элементов в списке, мы начнем отображать список заново.

Мне кажется иногда это может быть полезно, так что немного подправим Адаптер и его методы, таким образом, чтобы при создании класса, можно было явно указать хотим ли мы иметь дело с практически бесконечным списком
```kotlin
class ArticleAdapter(stream: InputStream, private val repeatInfinity: Boolean = false) {
    private val Int.current: Int get() = this % database.size()

    override fun onBindViewHolder(holder: BibTexViewHolder, position: Int) =
        if (repeatInfinity) holder.bind(database.getEntry(position.current))
        else holder.bind(database.getEntry(position))

    override fun getItemCount(): Int =
        if (repeatInfinity) 1488 * database.size() else database.size()
```

## Вывод
По ходу работы достиг все цели и решил все задачи, выше описал выводы по своей работе, пройдемся кратко по каждому пункту:
1. Научились импортировать сторонние библиотеки (в формате .JAR) в приложение, а также работать с ними в приложении
2. Ознакомились с одним из видов adapter-based views (RecyclerView)
3. Реализовали собственный Adapter, под нашу задачу, который работал как мост между Views и данными из библиотеки
4. Вновь поработали с xml-форматом (layouts)

[Дополнительные вопросы возникшие в ходе выполнения работы и решенные мной]
* Были включены в соответствующие разделы заданий.
