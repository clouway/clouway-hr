// The build command is building frontend application and is running continuous web build
package main

import (
	"log"
	"os"
	"os/exec"
)

func main() {
	errc := make(chan error)

	go startFrontend(errc)
	go startGruntWatch(errc)

	for err := range errc {
		log.Println(err)
	}
}

func startGruntWatch(errc chan error) {

	gruntWatch := exec.Command("grunt", "watch")
	gruntWatch.Dir = "."
	gruntWatch.Stdout = os.Stdout
	gruntWatch.Stderr = os.Stderr

	if err := gruntWatch.Start(); err != nil {
		errc <- err
		close(errc)
	}
}

func startFrontend(errc chan error) {

	cmd := exec.Command("go", "run", "frontend.go")
	cmd.Dir = "."
	cmd.Env = append(os.Environ(), "environment=test")
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	if err := cmd.Run(); err != nil {
		errc <- err
		close(errc)
	}
}
