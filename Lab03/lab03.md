# Лабораторная работа №3. Lifecycle компоненты. Навигация в приложении.

## Цели
* Ознакомиться с методом обработки жизненного цикла activity/fragment при помощи Lifecycle-Aware компонентов
* Изучить основные возможности навигации внутри приложения: создание новых activity, navigation graph

## Задачи  
### Задача 1. Обработка жизненного цикла с помощью Lifecycle-Aware компонентов  
#### Задание  
Ознакомьтесь с Lifecycle-Aware Components по документации: https://developer.android.com/topic/libraries/architecture/lifecycle и выполните codelabs (ссылка внизу страницы в разделе codelabs)
  
#### Указания  
В отчете укажите, какую новую полезную информацию/навыки удалось усвоить/получить в процессе выполнения задания. 
  
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
