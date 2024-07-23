# simulation
It's a simple services facility simulation. The app emulates clients entering the hairdresser and receiving a specific service. Cilent (circle) requires worker (traingle) with adequate color to attend to them.
# ui
UI was made using **JavaFX**. The simulation scene was made using **Scene Builder**, however the settings scene, because it needs a lot more interacion and synchronization, was made entirely in **Java**. Styles are written in **CSS**.
# synchronization
The clients and workers are represented by threads, which are synchronised using monitor with **locks** and **semaphores**.
