# Утилита фильтрации содержимого файлов
___
## Описание функционала
Утилита для фильтрации содержимого текстовых файлов содержащих: строки, целые и вещественные числа.
На вход через консоль подаются два текстовых файла и опции. В результате работы утилиты создаются файлы, содержащие
отфильтрованные данные. В зависимости от указанной опции поведение может меняться.
___
### Список опций
+ `-o` Опция для указания пути исходящих файлов. Работает с существующими директориями. Если будет указан 
несуществующий путь, возникнет ошибка.
+ `-p` Опция для добавления префикса к имени файла.
+ `-a` Опция для добавления данных в существующие файлы. Если попытаться добавить данные в несуществующие файлы, 
возникнет ошибка.
+ `-s` Опция для выведения краткой статистики. Содержит только количество элементов, записанных в исходящие файлы.
+ `-f` Опция для выведения полной статистики. Полная статистика для чисел дополнительно содержит минимальное и 
максимальное значения, сумму и среднее арифметическое значение. Полная статистика для строк, помимо их количества, 
содержит также размер самой короткой строки и самой длинной. Если таких строк несколько - они будут выведены.
___
### Особенности реализации
+ В программе настроено логирование с помощью Logback. Логи записываются в файл .\logs\app.log
+ Для работы с аргументами командной строки используется библиотека Apache Commons CLI.
+ Программа отработает и создаст файлы результатов, если в консоль будут поданы просто файлы без опций.
+ Формат подаваемых файлов в консоль **filename.txt**
+ Количество переданных файлов может быть произвольным.
+ Если были переданы не корректные аргументы в консоль, будет выведена подсказка:
```
  usage: app <options> file1.txt file2.txt
  -a         Append data to existing files
  -f         Display detailed statistics
  -o <arg>   Specify path for outgoing files
  -p <arg>   Add prefix to file name
  -s         Display brief statistics
  ```
+ Если один из переданных файлов окажется неверного формата или возникнет ошибка чтения, а второй будет валидным,
программа обработает только валидный файл. 
+ Названия файлов результатов по умолчанию: \
`integers.txt` для целых чисел.\
`floats.txt` для вещественных чисел.\
`strings.txt` для строк.
+ Файлы создаются по мере необходимости, если соответствующий тип данных присутствует во входных файлах. 
+ По умолчанию файлы результатов перезаписываются.
+ Целые числа ограничены диапазоном `long`, где минимальное значение `-9 223 372 036 854 775 808`, максимальное значение
`9 223 372 036 854 775 807`. Если выйти за границы этого диапазона, произойдет конвертация значения в экспоненциальную 
запись. То есть число будет считаться вещественным, а не целым.
___
### Требования
+ `Java: версия 21`
+ `Система сборки: Maven (apache-maven-3.9.9)`
___
### Зависимости
+ `commons-cli version 1.9.0` [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
+ `logback-core version 1.5.16` [LOGBACK](https://logback.qos.ch/)
+ `logback-classic version 1.5.16`
+ `slf4j-api version 2.0.16` [SLF4J](https://www.slf4j.org/)
+ `maven-shade-plugin version 3.5.0` [Apache Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/)
___
### Инструкция по запуску
#### Сборка с помощью Maven
+ Клонируйте репозиторий.
+ Откройте терминал в корневой директории проекта и выполните команду: `mvn package`
+ После успешной сборки в папке target появится исполняемый JAR-файл `ShiftSortUtil.jar`

#### Запуск утилиты
Для запуска утилиты выполните команду:

Например:
```
java -jar ShiftSortUtil.jar -s -a -p sample- in1.txt in2.txt
```
