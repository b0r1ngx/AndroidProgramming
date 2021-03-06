# Лабораторная работа №2. Activity Lifecycle. Alternative resources.

## Цели
* Ознакомиться с жизненным циклом Activity
* Изучить основные возможности и свойства alternative resources

## Задачи
### Задача 1. Activity
#### Задание
Продемонстрировать жизненный цикл Activity на любом нетривиальном примере.

P.S. Тривиальными считаются следующие примеры:
Создание/открытие/закрытие приложения (кроме случаев нестандартного завершения работы: SIGKILL, Force Stop, etc.)
Поворот экрана

Демонстрацию жизненного цикла Activity приведем на следующих примерах: 
* Получение уведомления из другого приложения
* Ответ на звонок
* Работа в многооконном режиме

Будем продолжать работу с приложением сделанным в Lab01, и модифицировать его по ходу выполнения работ.

Для того, чтобы легче было следить за жизненным циклом Activity, переопределим основной набор [callback'ов](https://developer.android.com/guide/components/activities/activity-lifecycle#lc) и добавим в каждый, обращение к классу **Log** с тем, что метод был вызван: **onCreate(), onStart(), onResume(), onPause(), onStop(), onDestroy()**. Система вызывает тот или иной метод, когда Activity переходит в новое состояние. При помощи **LogCat** будем следить, что происходит в тот или иной момент при нашей работе с устройством.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate() called")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "OnResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "OnPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() called")
    }

    companion object {
        internal const val TAG = "Activity-lifecycle"
    }
}
```

P.S. **LogCat** - модуль, встроенный в Android Studio, в который выгружается журнал системных сообщений (о работе, ошибках или другой информации устройства), когда мы, с помощью этого модуля подключаемся к устройству, за которым хотим наблюдать.

1. Получение уведомления о входящем звонке

Получая звонок, и получая об этом уведомление, мы все еще остаемся в Resumed state, что говорит о том, что уведомления не берут на себя фокус, когда мы находимся в каком-либо приложении.

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss1com.b0r1ngx.lab01_crop.jpg "Ситуация на устройстве")
![LogCat](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/1.png "LogCat")

2. Ответ на звонок

Если мы переходим в окно вызова звонка, вызываются методы onPause() и onStop() (строки 4 и 5)

После того как мы отменяем звонок, происходит автоматическое перенаправление обратно в приложение и вызываются методы onStart() и onResume() (строки 6 и 7)

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss2com.android.incallui_crop.jpg "Ситуация на устройстве")
![LogCat](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/2.png "LogCat")

Автоматическая переадресация после окончания звонка обратно в приложение

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss3com.b0r1ngx.lab01_crop.jpg "Ситуация на устройстве")

3. Работа в многооконном режиме

---

### Решение дополнительных вопросов (Указаний) для Задачи 1. Activity

* Объясните наблюдаемое поведение жизненного цикла Activity.

Жизненный цикл ведет себя так как и следовало ожидать по [документации](https://developer.android.com/guide/components/activities/activity-lifecycle), дополнительные пояснения могут быть даны мной по требованию.

Жизненный цикл Activity

![Жизненный цикл Activity](https://developer.android.com/guide/components/images/activity_lifecycle.png "Жизненный цикл Activity")

### Задача 2. Alternative Resources
#### Задание
Продемонстрируйте работу альтернативного ресурса (**MCC/MNC код**) на примере.

Для начала поясним, что такое MCC/MNC код:
* MCC (Mobile Country Code) - Мобильный код страны (всегда 3 цифры), например mcc250 - Россия на любом операторе. (Если код не определен, значение по умолчанию 0)
* MNC (Mobile Network Code) - Мобильный код сети (сетевой код) (код размера 2 цифры (по европейскому стандарту) или 3 (по североамериканскому)), например mcc250-mnc20 - Россия, с СИМ-картой Tele2. (Если код не определен, значение по умолчанию 0)

В работе с MCC/MNC кодами, возникают еще две аббревиатуры, которые хорошо бы понимать IMSI и PLMN
* IMSI (International Mobile Subscriber Identity) - Международный идентификатор мобильного абонента (15 цифр, но может быть короче), например 250-20-ХХХХХХХХХХ - Россия с СИМ-картой Tele2 и Х - уникальный идентификатор пользователя MSIN (Mobile Subscriber Identification Number).
* PLMN (Public Land Mobile Network) - Комбинация MCC-MNC кодов (5-6 цифр,  все по тем же стандартам).

Общий, поясняющий аббревиатуры пример:
* СИМ-карта имеющая следующий IMSI - 262330000000001, будет иметь PLMN - 262-33, MCC - 266, MNC - 33, СИМ-карта Германии, оператор связи Lyca Mobile.

Углубляясь, можно познать еще такую особенность, что для работающего телефона (без включенного режима полета и зарегистрированного в сети) одновременно существует 2 MCC/MNC кода, одна пара являются тем, что записано на СИМ-карте, а другая может быть выдана сетью или вышкой сотовой связи, в котором сейчас находится работающее устройство.


В примере, который приводится в документации, к описанию данного альтернативного ресурса, говорится о том, что данные коды можно использовать для того, чтобы внедрить в свое приложение некоторые официальные юридические документы (государственные, территориальные и пр.), специфические для конкретной страны в которой находится устройство или специфически отобранные под конкретную сеть (определяющуюся СИМ-картой или той сетью использующейся в устройстве).

Конкретный пример:
* Бизнес-решение. Собирать информацию о странах/сетях, которыми пользуются пользователи приложения (как общую, так и Real-time (с запущенными в данный момент устройствами)).
* Приложение, работающее только для абонентов конкретной сети (или работающее для любой сети, но связывающее, конкретно людей с одним оператором)

Также могут возникнуть вопросы:

* Как получить эту информацию?

Есть специальный класс [**TelephonyManager**](https://developer.android.com/reference/android/telephony/TelephonyManager), чтобы иметь доступ ко всей информации, которую можно получить при помощи данного класса, нужно объявить соответствующие разрешения в файле манифеста.

Получение MCC/MNC кодов, не требует этих разрешений, и может быть получена без каких-либо разрешений.

* Каким образом определяются данные коды, если СИМ-карты в устройстве две?

1. При обращении в некоторых методах, включена переменная slotIndex, позволяющая взять информацию с конкретной СИМ-карты, установленной в конкретный слот.
2. Взята напрямую из информации об устройстве, какая СИМ-карта определена как первоочередная (или в большинстве случаях единственная) (устанавливается пользователем в настройках).
3. *А также, по моему мнению, это может быть определено на стадии авторизации пользователем в приложение.*

Какой код, какой стране и оператору принадлежит можно более подробно узнать на этом [сайте](https://cellidfinder.com/mcc-mnc), более подробную информацию можно найти [здесь](https://en.wikipedia.org/wiki/Mobile_country_code).

Пример реализованный мною, запущенный на моем смартфоне, выдающий ссылку на документ РФ, так как мой оператор, привязан к РФ.

Так же, считаю обязательным, привести пример, в том же приложении, но на спроектированном пользователе, который находится в Германии, а значит MCC - 266, и получали бы все в том же приложении, но уже ссылку на другой источник, для пользователей из Германии.

### Задача 3. Best-matching resource
#### Задание
Для заданного набора альтернативных ресурсов, предоставляемых приложением, и заданной конфигурации устройства объясните, какой ресурс будет выбран в конечном счете. Ответ докажите.  

Разобраться c данной задачей нам поможет изучение работы алгоритма [Best-matching resource](https://developer.android.com/guide/topics/resources/providing-resources#BestMatch), в отчете приведем аналогичные рассуждения (пошаговое исполнение алгоритма) и отсечение им не подходящих ресурсов.

А теперь переходим к заданию

```
Конфигурация устройства:
LOCALE_LANG: fr
LOCALE_REGION: rUS
SCREEN_SIZE: normal
SCREEN_ASPECT: long
ROUND_SCREEN: notround
ORIENTATION: land
UI_MODE: appliance
NIGHT_MODE: night
PIXEL_DENSITY: xxhdpi
TOUCH: finger
PRIMARY_INPUT: 12key
NAV_KEYS: wheel
PLATFORM_VER: v27

Конфигурация ресурсов:
(default)
fr-normal-port-finger-12key-v27
fr-round-port-car-notnight-notouch
rUS-land-night
long-land-notnight-xxxhdpi-trackball-v26
normal-notround-vrheadset-night-v26
en-port-notouch
notround-television
fr-rUS-xlarge-round-desk-xxxhdpi-finger-v25
rUS-v27
```
У каждого алгоритма есть шаги исполнения, так и мы, пойдем по шагам:

1. Удаление ресурсов, противоречащих конфигурации устройства.

Будут убраны следующие ресурсы:

* fr-normal-port-finger-12key-v27, противоречит ORIENTATION: land
* fr-round-port-car-notnight-notouch, противоречит ORIENTATION: land
* long-land-notnight-xxxhdpi-trackball-v26, противоречит NIGHT_MODE: night(и точно противоречит NAV_KEYS: wheel!) (хотелось бы уточнить, насчет notnight и xxxhdpi, на первом этапе, точно не отсекаются разногласия по поводу PIXEL_DENSITY, а насчет notnight, по идее если устройство в night режиме, а приложение требует notnight, оно тоже запуститься, но будет в notnight режиме, но так как мы выбираем лучший оно здесь отсечется)
* normal-notround-vrheadset-night-v26, противоречит UI_MODE: appliance
* en-port-notouch, противоречит ORIENTATION: land
* notround-television, противоречит UI_MODE: appliance
* fr-rUS-xlarge-round-desk-xxxhdpi-finger-v25, противоречит SCREEN_SIZE: normal

Остаются следующие кандидаты на выбор:

* (default)
* rUS-land-night
* rUS-v27

2. Выбирается квалификатор/Выбирается следующий квалификатор с наивысшим приоритетом в [списке по таблице](https://developer.android.com/guide/topics/resources/providing-resources#table2) (начиная с MCC/MNC кода, затем язык и регион и т.д.)

3. Содержится ли данный квалификатор в каком-либо ресурсе?
* Если не содержится, возвращаемся к шагу 2 и переходим к следующему квалификатору (в нашем примере, ответ "нет", будет до тех пор, пока не будет достигнет квалификатор языка) 
* Если содержится, переходим к шагу 4

4. Удаление ресурсов, в которых нет этого квалификатора.

В нашем примере произойдет удаление всех ресурсов, в которых нет упоминания о языке или регионе, а это:
* (default)

5. Возвращаемся и повторяем шаги 2, 3 и 4, пока только один ресурс не останется.

В нашем примере произойдет удаление следующего ресурса:
* rUS-v27

После чего останется лишь один ресурс
* rUS-land-night

Он и будет выбран.

### Задача 4. Сохранение состояние Activity
#### Задание
Студент написал приложение: [continuewatch](continuewatch). Это приложение [по заданию](https://github.com/b0r1ngx/AndroidProgramming/blob/main/Lab02/lab02.md#%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BD%D0%B0-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-continuewatch) должно считать, сколько секунд пользователь провел в этом приложении. (В папку с лабораторной работой, помещено рабочее, исправленное мной, приложение)

Найдите ошибки в этом приложении и исправьте их.  

#### Задание на приложение continuewatch
Разработать приложение, которое считает сколько секунд пользователь провел в приложении, т.е.:
* Приложение считает секунды, когда оно отображается на экране
* Приложение не считает секунды, когда оно не отображается на экране
* Значение счетчика сохраняется при перезапуске приложения

### Решение дополнительных вопросов (Указаний) для Задачи 4. Сохранение состояние Activity
* Для поиска ошибок запустите приложение на устройстве или эмуляторе и проверьте, делает ли приложение то, что от него ожидается (фактически, необходимо выполнить ручное тестирование приложения)

При запуске на устройстве можем обнаружить следующие несоответствия, требуемые [по заданию](https://github.com/b0r1ngx/AndroidProgramming/blob/main/Lab02/lab02.md#%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BD%D0%B0-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-continuewatch):
* Приложение считает секунды, даже тогда, когда не отображается на экране (находится не в фокусе) (quickmark: нужно реагировать в моменты попадания в состояние Paused, а также Resumed)
* Значение счетчика не сохраняется при перезапуске приложения (перезапуск, вызов метода onDestroy(), как самим устройством, так и вручную)

Добьемся исправления этих ошибок после ответа на дополнительные вопросы

* Для исправления ошибок могут понадобиться методы [Activity::onSaveInstanceState/onRestoreInstanceState](https://developer.android.com/guide/components/activities/activity-lifecycle#save-simple,-lightweight-ui-state-using-onsaveinstancestate)

Действительно эти методы необходимы в следующих случаях:
* метод **onSaveInstanceState()** вызывается, когда **Activity** переходит в состояние Stopped, после вызова метода onStop(), чтобы можно было сохранить информацию о  состоянии (activity can save state information to an instance state bundle).
* метод **onRestoreInstanceState()** вызывается, когда **Acitivity** воссоздается после того, как она была ранее уничтожена, можно восстановить сохраненное состояние экземпляра из Bundle, который система передает вашей активности ([система вызывает метод только если состояние было ранее сохраненно] (при помощи onSaveInstanceState())

При анализе кода также были замечены недочеты, которые стоило бы исправить:
* Не нужная перезапись текста в блоках TextView в обоих положениях экрана
* В горизонтальном положении, нет привязки блока TextView, поэтому он уезжает

Исправления, выполненные, для корректной работы [continuewatch](continuewatch).

Добавление/изменение переменных: 
* RESUMED_STATE = true - определяет необходимость работы потока, в тот или иной момент работы приложения
* sharedSeconds: SharedPreferences - хранение количества секунд при перезагрузке (A SharedPreferences object points to a file containing key-value pairs and provides simple methods to read and write them)
* backgroundThread = Thread { while(RESUMED_STATE) } - работа потока, имитирующего таймер
* SECONDS_KEY = "sec" - ключ, хранящий наше количество прошедших секунд, при обращении к /sharedSeconds

Необходимость в переопределении **onResume(), onPause()** в том, чтобы понимать когда наше приложение находится в фокусе, а когда выходит из него, в них же добавлена следующая логика:
```kotlin
    override fun onResume() {
        super.onResume()
        RESUMED_STATE = true
    }

    override fun onPause() {
        super.onPause()
        RESUMED_STATE = false
    }
```

Так же были переопределены методы, упомянутые в указаниях, и в них была добавлена следующая логика:
```kotlin
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SECONDS_KEY, secondsElapsed)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.run {
            secondsElapsed = getInt(SECONDS_KEY)
        }
    }
```

И для корректной работы приложения при перезапуске, также была добавлена следующая логика в **onStart(), onStop()** (возможно возникнет вопрос, почему это было сделано не в методе **onDestroy()**, при сохранении секунд, в данной функции, не корректно сохранялись секунды).
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        sharedSeconds = getDefaultSharedPreferences(this)
        secondsElapsed = sharedSeconds.getInt(SECONDS_KEY, secondsElapsed)
        textSecondsElapsed.text = getString(R.string.SE, secondsElapsed)
        backgroundThread.start()
    }

    override fun onStop() {
        super.onStop()
        with (sharedSeconds.edit()) {
            putInt(SECONDS_KEY, secondsElapsed)
            apply()
        }
    }
```

Так же добавил кнопку и небольшую логику в нее, для перезапуска таймера
```kotlin
    fun onClick(view: View) {
        secondsElapsed = 0
        Toast.makeText(applicationContext, getString(R.string.reset_toast), Toast.LENGTH_SHORT).show()
    }
```

Приложение полностью удовлетворяет своей работе, требуемой [по заданию](https://github.com/b0r1ngx/AndroidProgramming/blob/main/Lab02/lab02.md#%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BD%D0%B0-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-continuewatch). (ниже небольшое превью, в скриншотах)

![ContinueWatch-port](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss1_continuewatch.jpg "ContinueWatch-port")
![ContinueWatch-land](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss2_continuewatch.jpg "ContinueWatch-land")


## Вывод
По ходу работы достиг все цели и решил все задачи, выше описал вывод по своей работе, пройдемся кратко по каждому пункту:
1. Четко разобрались с жизненным циклом Activity 
2. Углубились в работу с альтернативными ресурсами
3. Разобрались в работе алгоритма Best-matching resource
4. На конкретном примере, решили проблему логики, связанную с жизненным циклом Activity

[Дополнительные вопросы возникшие в ходе выполнения работы и решенные мной]
* Были включены в соответствующие разделы заданий.
