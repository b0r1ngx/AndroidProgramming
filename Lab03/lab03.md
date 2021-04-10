# Лабораторная работа №3. Lifecycle компоненты. Навигация в приложении.

## Цели
* Ознакомиться с методом обработки жизненного цикла activity/fragment при помощи Lifecycle-Aware компонентов
* Изучить основные возможности навигации внутри приложения: создание новых activity, navigation graph

## Задачи  
### Задача 1. Обработка жизненного цикла с помощью Lifecycle-Aware компонентов  
#### Задание  
Ознакомьтесь с Lifecycle-Aware Components по [документации](https://developer.android.com/topic/libraries/architecture/lifecycle) и выполните [codelabs](https://developer.android.com/codelabs/android-lifecycles#0)

Codelabs, как и статья из документации знакомит Нас со следующими lifecycle-aware architecture components для создания приложений
* **ViewModel - класс** отвечающий за подготовку и распределение разного рода данных для **Activity или Fragment**. Он также обрабатывает связи **Activity/Fragment** c остальной частью приложения. **ViewModel** можно и нужно создавать только тогда, когда имеется область ассоциации с **Activity/Fragment**, **ViewModel** будет жить, пока существует эта область. Например, если в качестве ассоциации выбрана **Activity, то ViewModel** будет жить пока **Activity** не завершиться (попадет в состояние **Finished**).
* **LifecycleOwner - интерфейс** реализованный при помощи **классов AppCompatActivity и Fragment**. С помощью него, мы можем подписывать какие либо компоненты, на объект, который наследуются от этого интерфейса, чтобы наблюдать за изменениями в жизнненом цикле реализуемого. You can subscribe other components to owner objects which implement this interface, to observe changes to the lifecycle of the owner.
* **LiveData - класс** allows you to observe changes to data across multiple components of your app without creating explicit, rigid dependency paths between them. LiveData respects the complex lifecycles of your app components, including activities, fragments, services, or any LifecycleOwner defined in your app. LiveData manages observer subscriptions by pausing subscriptions to stopped LifecycleOwner objects, and cancelling subscriptions to LifecycleOwner objects that are finished. (LiveData is a special observable class which is lifecycle-aware, and only notifies active observers. LiveData - класс наблюдателя, который учитывает жизненный цикл и уведомляет только активных наблюдателей.)

В codelabs мы попробовали реализовать на примерах каждый из компонентов, описанных выше. 

По ходу выполнения codelabs, мы пошагово продвигаемся и интегрирум различные архитектурные компоненты, все больше разбираясь как структурировать свои приложения для того, чтобы они были надежными и поддерживаемыми, а также могли подлежать тестированию отдельных компонентов, в конкретные периоды времени.

#### Указания  
Укажите, какую новую полезную информацию/навыки удалось усвоить/получить в процессе выполнения задания.
* Хранение ссылок на данные **Context/View** в **ViewModel** может привести к утечкам памяти. Нужно избегать полей, который ссылаются на экземпляры **классов Context/View**. Метод **onCleared()** может быть полезным для отмены подписки или очистки на другие объекты с более длительным жизненным циклом, но не для очистки ссылок на объекты **Context/View**.
* Вместо того, чтобы изменять **View** во **ViewModel**, мы настраиваем **Activity/Fragment** так, чтобы была возможность, как бы наблюдать за источником данных, получая их, когда они изменились. Такая реализация называется **the observer pattern**. (чтобы представить данные как наблюдаемые, оберните тип в класс LiveData.)
* LifecycleOwner is an interface that is used by any class that has an Android lifecycle. Practically, it means that the class has a Lifecycle object that represents its lifecycle.

### Задача 2. Навигация (startActivityForResult)
#### Задание
Реализуйте навигацию между экранами одного приложения согласно эскизу с помощью **Activity, Intent и startActivityForResult()**.

![эскиз](https://raw.githubusercontent.com/b0r1ngx/AndroidProgramming/main/Lab02/images/activities.png "эскиз")

### Задача 3. Навигация (флаги Intent/атрибуты Activity)
#### Задание
Решите предыдущую задачу без использования **startActivityForResult()**, а с помощью Activity, Intent и флагов Intent либо атрибутов Activity


### Задача 4. Навигация (флаги Intent/атрибуты Activity)
Дополните граф навигации новым(-и) переходом(-ами) с целью демонстрации какого-нибудь (на свое усмотрение) атрибута Activity или флага Intent, который еще не использовался для решения задачи. Поясните пример и работу флага/атрибута.

Ограничение на размер backstack к этому и следующему заданию не применяется.

### Задача 5. Навигация (Fragments, Navigation Graph) 
Решите предыдущую задачу (с расширенным графом) с использованием navigation graph. Все Activity должны быть заменены на фрагменты, кроме Activity 'About', которая должна остаться самостоятельной Activity.
В отчете сравните все решения.

#### Указания
* Ознакомьтесь с navigation graph по документации (https://developer.android.com/guide/navigation/navigation-getting-started) или видеоуроку (https://classroom.udacity.com/courses/ud9012 Lesson 3 “App Navigation”))
* Для отображения layout в fragment используйте метод `onCreateView` (см. пример: https://developer.android.com/guide/components/fragments#UI).
