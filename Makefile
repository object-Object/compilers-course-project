examples/%: FORCE
	./gradlew run --quiet --console=plain --args="examples/$*.hexpattern"

.PHONY: FORCE
FORCE:
