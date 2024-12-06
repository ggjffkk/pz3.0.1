
Work stealing використовує ForkJoinPool, щоб поділити задачу на підзадачі, які автоматично розподіляються між потоками.
Work dealing використовує ExecutorService, визначає кількість задач.

Час виконання різний, так як work stealing має нерівномірний розпоідл самих задач, 
а work dealing більше концентрується на поблокових задачах.


![image](https://github.com/user-attachments/assets/21ef8c1c-4e0f-4b62-97fb-425194cbfd8c)
![image](https://github.com/user-attachments/assets/c90854e4-9ff6-47a6-b171-70cc56351dab)
