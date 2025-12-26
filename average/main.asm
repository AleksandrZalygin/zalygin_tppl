section .data
    x dd 5, 3, 2, 6, 1, 7, 4
    y dd 0, 10, 1, 9, 2, 8, 5
    count equ 7
    newline db 0xA

section .bss
    buffer resb 20

section .text
    global _start

_start:
    xor rbx, rbx
    xor r12, r12
    mov rcx, count

calculate_loop:
    mov eax, [x + rbx * 4]
    sub eax, [y + rbx * 4]
    movsxd rax, eax
    add r12, rax
    inc rbx
    loop calculate_loop

    mov rax, r12
    mov rbx, count
    cqo
    idiv rbx

    call print_result

    mov rax, 60
    xor rdi, rdi
    syscall

print_result:
    cmp rax, 0
    jge convert_to_string
    push rax
    mov rax, 45
    mov [buffer], al
    mov rax, 1
    mov rdi, 1
    mov rsi, buffer
    mov rdx, 1
    syscall
    pop rax
    neg rax

convert_to_string:
    mov rbx, 10
    mov rcx, buffer + 19
    mov byte [rcx], 0
    dec rcx

convert_loop:
    xor rdx, rdx
    div rbx
    add dl, '0'
    mov [rcx], dl
    dec rcx
    test rax, rax
    jnz convert_loop

    inc rcx
    mov rsi, rcx
    lea rdx, [buffer + 20]
    sub rdx, rcx
    mov rax, 1
    mov rdi, 1
    syscall

    mov rax, 1
    mov rdi, 1
    mov rsi, newline
    mov rdx, 1
    syscall
    ret