//package asot.me.rest.controller;
//
//import asot.me.rest.service.BootStrapService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/data")
//@RequiredArgsConstructor
//public class BootStrapController {
//    private final BootStrapService bootStrapService;
//
//    @DeleteMapping("/all")
//    public ResponseEntity<Void> deleteAll() {
//        bootStrapService.deleteAll();
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<Void> createAll() {
//        bootStrapService.createAll();
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/all/{param}")
//    public ResponseEntity<Void> deleteByParam(@PathVariable String param) {
//        bootStrapService.deleteByParam(param);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/all/{params}")
//    public ResponseEntity<Void> deleteByParam(@PathVariable String[] params) {
//        bootStrapService.deleteByMultiParams(params);
//        return ResponseEntity.ok().build();
//    }
//
//}