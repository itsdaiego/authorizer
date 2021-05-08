build:
	@docker build --target application  -t authorizer .

build-tests:
	@docker build --target application-test -t authorizer-test .

run-app:
	@docker run -i authorizer <&0


run-tests:
	@docker run authorizer-test



.PHONY: build build-tests run-app run-tests

