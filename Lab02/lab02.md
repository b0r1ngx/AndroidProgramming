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

Для того, чтобы легче было следить за жизненным циклом Activity, переопределим основной набор callback'ов и в каждом будем писать информацию через **Class Log** о том, что метод был вызван: **onCreate(), onStart(), onResume(), onPause(), onStop(), onDestroy()**. Система вызывает тот или иной метод, когда Activity переходит в новое состояние. При помощи **LogCat** будем следить, что происходит в тот или иной момент при нашей работе с устройством.

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

1. Получение уведомления о звонке
Получая звонок, и получая об этом уведомление, о том что нам звонят, мы все еще остаемся в Resumed state, что говорит о том, что уведомления не берут на себя фокус, когда появляются.

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss1com.b0r1ngx.lab01.jpg =100x20)
![LogCat](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/1.png "LogCat")

2. Ответ на звонок
Если мы переходим в окно вызова звонка, вызываются методы onPause() и onStop() (строки 4 и 5)

после того как мы отменяем звонок, происходит автоматическое перенаправление обратно в приложение и вызываются методы onStart() и onResume() (строки 6 и 7)

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss2com.android.incallui.jpg "Ситуация на устройстве")
![LogCat](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/2.png "LogCat")

Автоматическая переадресация после окончания звонка обратно в приложение

![Ситуация на устройстве](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/ss3com.b0r1ngx.lab01.jpg "Ситуация на устройстве")

3.

### Решение дополнительных вопросов (Указаний) для Задачи 1. Activity

* Объясните наблюдаемое поведение жизненного цикла Activity.

### Задача 2. Alternative Resources
#### Задание
Продемонстрируйте работу альтернативного ресурса (**MCC/MNC код**) на примере.


### Задача 3. Best-matching resource
#### Задание
Для заданного набора альтернативных ресурсов, предоставляемых приложением, и заданной конфигурации устройства объясните, какой ресурс будет выбран в конечном итоге. Ответ докажите.  

Разобраться c данной задачей нам поможет изучение работы алгоритма [Best-matching resource](https://developer.android.com/guide/topics/resources/providing-resources#BestMatch), в отчете приведем аналогичные рассуждения (пошаговое исполнение алгоритма) и отсечение им

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


### Задача 4. Сохранение состояние Activity.
#### Задание
Студент написал приложение: [continuewatch](continuewatch). Это приложение [по заданию](continuewatch/README.md) должно считать, сколько секунд пользователь провел в этом приложении.  

Найдите ошибки в этом приложении и исправьте их.  

### Указания:  
* Для поиска ошибок запустите приложение на устройстве или эмуляторе и проверьте, что приложение делает то, что он него ожидается (фактически, необходимо выполнить ручное тестирование приложения).
* При тестировании обращайте внимание на правильность обработки lifecycle методов.  
* Для исправления ошибок могут понадобиться методы Activity::onSaveInstanceState/onRestoreInstanceState (https://developer.android.com/guide/components/activities/activity-lifecycle#save-simple,-lightweight-ui-state-using-onsaveinstancestate)

## Вывод
По ходу работы достиг все цели и решил все задачи, выше описал вывод по своей работе, пройдемся кратко по каждому пункту:
1. a
2. b
3. c
4. d

[Дополнительные вопросы возникшие в ходе выполнения работы и решенные мной]
